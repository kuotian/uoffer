package com.hxxzt.recruitment.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.authentication.JWTToken;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.exception.PenintException;
import com.hxxzt.recruitment.common.manager.UserManager;
import com.hxxzt.recruitment.common.properties.PenintProperties;
import com.hxxzt.recruitment.common.utils.*;
import com.hxxzt.recruitment.common.vo.ResultVo;
import com.hxxzt.recruitment.system.entity.*;
import com.hxxzt.recruitment.system.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/adminApi/auth")
public class AuthController {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysLoginLogService loginLogService;

    @Autowired
    private PenintProperties properties;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ISysUserRoleService userRoleService;

    /**
     * 登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    @PostMapping("/doLogin")
    public ResultVo login(String username, String password, String code, String uuid, HttpServletRequest request) throws PenintException {

        String verifyKey = Constant.RM_CAPTCHA_CODE_KEY + uuid;
        String captcha = redisUtil.getCacheObject(verifyKey);
        redisUtil.del(verifyKey);

        if (captcha == null) {
            return ResultVo.failed(201, "验证码失效");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            return ResultVo.failed(201, "验证码错误");
        }

        username = StringUtils.lowerCase(username);
        password = MD5Util.encrypt(username, password);

        final String errorMessage = "用户名或密码错误";
        SysUser user = userManager.getUser(username);

        if (user == null) {
            return ResultVo.failed(201, errorMessage);
        }
        if (!StringUtils.equalsIgnoreCase(user.getPassword(), password)) {
            return ResultVo.failed(201, errorMessage);
        }
        if (Constant.STATUS_LOCK.equals(user.getStatus())) {
            return ResultVo.failed(201, "账号已被锁定,请联系管理员！");
        }


        Integer userId = user.getUserId();
        String ip = IPUtil.getIpAddr(request);
        String address = AddressUtil.getCityInfo(ip);
        // 更新用户登录时间
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLastLoginTime(new Date());
        sysUser.setLastLoginIp(ip);
        userService.updateById(sysUser);


        // 拿到token之后加密
        String sign = JWTUtil.sign(username, password, userId);
        String token = PenintUtil.encryptToken(sign);
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getJwtTimeOut());
        String expireTimeStr = DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        // 将登录日志存入日志表中
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setIp(ip);
        loginLog.setAddress(address);
        loginLog.setLoginTime(new Date());
        loginLog.setUsername(username);
        loginLog.setUserId(userId);
        loginLogService.save(loginLog);

        saveTokenToRedis(username, jwtToken, ip, address);
        JSONObject data = new JSONObject();
        data.put("Authorization", token);

        // 将用户配置及权限存入redis中
        userManager.loadOneUserRedisCache(userId);
        return ResultVo.oK(data);
    }


    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/userInfo")
    public ResultVo userInfo(HttpServletRequest request) {

        Integer userId = JWTUtil.getUserId(request);
        // 用户详细信息
//        SysUser user = userService.getById(userId);
        SysUser user = userManager.getUser(userId);

        // 角色集合
//        Set<String> roles = roleService.selectRolePermissionByUserId(userId);
        Set<String> roles = userManager.getUserRoles(userId);
        // 权限集合 getUserPermissions
//        Set<String> permissions = menuService.findUserPermissionsByUserId(userId);
        Set<String> permissions = userManager.getUserPermissions(userId);

        JSONObject data = new JSONObject();

        data.put("user", user);
        data.put("roles", roles);
        data.put("permissions", permissions);
        return ResultVo.oK(data);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public ResultVo getRouters(HttpServletRequest request) {

        Integer userId = JWTUtil.getUserId(request);
        List<SysMenu> menuList = menuService.selectMenuTreeByUserId(userId);
        return ResultVo.oK(menuService.buildMenus(menuList));
    }

    @PostMapping("/logout")
    public ResultVo logout(HttpServletRequest request) {
        // 取token
        String token = request.getHeader("Authorization");
        this.kickout(token, null);
        return ResultVo.oK();

    }

    /**
     * 强退用户
     *
     * @param token 用户主动注销时使用token
     *              zSetId 强退用户时用zSetId
     * @throws Exception
     */
    @DeleteMapping("/forcedAccountOut/{zSetId}")
    @RequiresPermissions("system:online:forcedAccountOut")
    @Log(module = "用户管理", operation = "强退用户")
    public ResultVo kickout(@NotBlank(message = "{required}") String token, @PathVariable String zSetId) {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisUtil.sortRange(Constant.RM_ACTIVE_USERS_ZSET_PREFIX, Long.valueOf(now), Long.valueOf(DateUtil.getNowAddToken(properties.getShiro().getJwtTimeOut())));
        ActiveUser kickoutUser = null;
        String kickoutUserString = "";
        if (!StringUtils.isEmpty(zSetId)) {
            for (String userOnlineString : userOnlineStringSet) {
                ActiveUser activeUser = null;
                try {
                    activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                if (StringUtils.equals(activeUser.getId(), zSetId)) {
                    kickoutUser = activeUser;
                    kickoutUserString = userOnlineString;
                }
            }
        } else if (!StringUtils.isEmpty(token)) {
            for (String userOnlineString : userOnlineStringSet) {
                ActiveUser activeUser = null;
                try {
                    activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                if (StringUtils.equals(activeUser.getToken(), token)) {
                    kickoutUser = activeUser;
                    kickoutUserString = userOnlineString;
                }
            }
        }
        if (kickoutUser != null && StringUtils.isNotBlank(kickoutUserString)) {
            // 删除 zset中的记录
            redisUtil.zrem(Constant.RM_ACTIVE_USERS_ZSET_PREFIX, kickoutUserString);
            // 删除对应的 token缓存
            redisUtil.del(Constant.RM_TOKEN_CACHE + kickoutUser.getToken() + StringPool.UNDERSCORE + kickoutUser.getIp());
            return ResultVo.oK();
        }
        return ResultVo.failed(201, "强退用户失败");
    }


    /**
     * 查询在线用户
     *
     * @param username
     * @return
     * @throws Exception
     */
    @GetMapping("/online")
    @RequiresPermissions("system:online:view")
    public ResultVo userOnline(String username) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisUtil.sortRange(Constant.RM_ACTIVE_USERS_ZSET_PREFIX, Long.valueOf(now), Long.valueOf(DateUtil.getNowAddToken(properties.getShiro().getJwtTimeOut())));
        List<ActiveUser> activeUsers = new ArrayList<>();
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            activeUser.setToken(null);
            if (StringUtils.isNotBlank(username)) {
                if (StringUtils.equalsIgnoreCase(username, activeUser.getUsername())) {
                    activeUsers.add(activeUser);
                }
            } else {
                activeUsers.add(activeUser);
            }
        }
        return ResultVo.oK(activeUsers);
    }

    /**
     * 查询redis中存储的zset集合id,
     * 此方法 判断在线用户列表中哪条记录为当前账号
     *
     * @throws Exception
     */
    @GetMapping("/getIsMe")
    public ResultVo getIsMe(HttpServletRequest request) throws Exception {
        // 取token
        String token = request.getHeader("Authorization");
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisUtil.sortRange(Constant.RM_ACTIVE_USERS_ZSET_PREFIX, Long.valueOf(now), Long.valueOf(DateUtil.getNowAddToken(properties.getShiro().getJwtTimeOut())));
//        ActiveUser activeUsers = new ArrayList<>();
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            if (StringUtils.isNotBlank(token)) {
                if (StringUtils.equalsIgnoreCase(token, activeUser.getToken())) {
                    return ResultVo.oK(activeUser);
                }
            }
        }
        return ResultVo.failed(201, "查询失败，无此登录用户");

    }

    /**
     * 登录用户存储token
     * 构建在线用户
     */
    private String saveTokenToRedis(String username, JWTToken token, String ip, String address) {

        // 构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(username);
        activeUser.setIp(ip);
        activeUser.setToken(token.getToken());
        activeUser.setLoginAddress(address);

        // zset 存储登录用户，score 为过期时间戳
        try {
            redisUtil.zSet(Constant.RM_ACTIVE_USERS_ZSET_PREFIX, mapper.writeValueAsString(activeUser), Double.valueOf(token.getExipreAt()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // redis 中存储这个加密 token，key = 前缀 + 加密 token + .ip
        redisUtil.set(Constant.RM_TOKEN_CACHE + token.getToken() + StringPool.UNDERSCORE + ip, token.getToken(), properties.getShiro().getJwtTimeOut());

        return activeUser.getId();
    }

    @PostMapping("/register")
    public ResultVo register(@RequestParam String username, @RequestParam String password, @RequestParam String email, @RequestParam String mobile) {
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(MD5Util.encrypt(username, password));
        user.setEmail(email);
        user.setMobile(mobile);
        user.setCreateTime(new Date());
        userService.save(user);
        // 注册用户赋予注册用户角色
        Integer userId = user.getUserId();
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(103); // 注册用户的Id为103
        userRoleService.save(userRole);
        return ResultVo.oK();
    }


}