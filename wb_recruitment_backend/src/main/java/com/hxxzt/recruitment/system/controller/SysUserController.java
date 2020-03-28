package com.hxxzt.recruitment.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.common.manager.UserManager;
import com.hxxzt.recruitment.common.utils.*;
import com.hxxzt.recruitment.common.vo.ResultVo;
import com.hxxzt.recruitment.system.entity.SysUser;
import com.hxxzt.recruitment.system.entity.SysUserRole;
import com.hxxzt.recruitment.system.service.ISysUserRoleService;
import com.hxxzt.recruitment.system.service.ISysUserService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@RestController
@RequestMapping("/adminApi/sysUser")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/list")
    @RequiresPermissions("system:user:view")
    public ResultVo list(QueryRequest queryRequest, SysUser user) {
        return ResultVo.oK(userService.queryFuzz(queryRequest, user));
    }

    /**
     * 修改账户状态，禁用或者取消
     *
     * @param user
     * @param request
     * @return
     */
    @PutMapping("/changeStatus")
    @Log(module = "用户管理", operation = "修改账户状态")
    public ResultVo changeStatus(@RequestBody SysUser user, HttpServletRequest request) {
        Integer userId = JWTUtil.getUserId(request);
        if (userManager.isAdmin(userId)) {

            userService.updateById(user);

            // 清除token
            redisUtil.del(Constant.RM_TOKEN_CACHE + userService.getById(user.getUserId()).getUsername());
            return ResultVo.oK();
        } else {
            return ResultVo.failed(201, "无权限，admin 用户有此权限");
        }
    }

    /**
     * 根据ID查询用户详情
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    @RequiresPermissions("system:user:query")
    public ResultVo getUserInfo(@PathVariable Integer userId) {
        SysUser info = userService.getInfoById(userId);
        String roleId = info.getRoleId();
        if (StringUtils.isNotEmpty(roleId)) {
            String[] str = roleId.split(",");
            Integer[] aftIdArray = (Integer[]) ConvertUtils.convert(str, Integer.class);
            info.setRoleIds(aftIdArray);
        }
        return ResultVo.oK(info);
    }


    /**
     * 新增用户
     *
     * @param sysUser
     * @return
     */
    @PostMapping
    @RequiresPermissions("system:user:add")
    @Log(module = "用户管理", operation = "新增用户")
    public ResultVo add(@RequestBody SysUser sysUser) {
        // 查询用户名是否存在数据库中
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUsername, sysUser.getUsername());
        SysUser user = userService.getOne(lambdaQueryWrapper);
        if (user == null) {
            sysUser.setCreateTime(new Date());
            // 设置默认密码1234qwer
            sysUser.setPassword(MD5Util.encrypt(sysUser.getUsername(), Constant.DEFAULT_PASSWORD));
            userService.insertSelective(sysUser);
            // 插入返回ID
            Integer userId = sysUser.getUserId();
            // 新增角色用户关联
            if (sysUser.getRoleIds().length > 0) {
                userRoleService.insertByRoleList(userId, sysUser.getRoleIds());
            }
            // 存入缓存
            userManager.loadOneUserRedisCache(userId);
            return ResultVo.oK("用户:" + sysUser.getUsername() + "新增成功，默认密码为：" + Constant.DEFAULT_PASSWORD);
        } else {
            return ResultVo.failed(201, "用户新增失败，用户名：" + sysUser.getUsername() + "已存在");
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userIds}")
    @RequiresPermissions("system:user:remove")
    @Log(module = "用户管理", operation = "删除用户")
    public ResultVo remove(@PathVariable Integer[] userIds) {
        boolean flag = false;
        for (Integer userId : userIds) {
            if (!userManager.isAdmin(userId)) {
                userService.removeById(userId);
                // 删除用户角色表中对应关系
                LambdaQueryWrapper<SysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(SysUserRole::getUserId, userId);
                userRoleService.remove(lambdaQueryWrapper);
                // 删除缓存
                userManager.deleteUserRedisCache(userId);
            } else {
                flag = true;
            }
        }
        if (flag) {
            return ResultVo.failed(201, "admin用户不能删除");
        } else {
            return ResultVo.oK();
        }
    }

    /**
     * 修改用户
     *
     * @param sysUser
     * @return
     */
    @PutMapping
    @RequiresPermissions("system:user:edit")
    @Log(module = "用户管理", operation = "修改用户")
    public ResultVo edit(@RequestBody SysUser sysUser) {
        userService.updateById(sysUser);
        // 删除角色，重新赋予
        LambdaQueryWrapper<SysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserRole::getUserId, sysUser.getUserId());
        userRoleService.remove(lambdaQueryWrapper);

        Integer[] roleIds = sysUser.getRoleIds();
        if (StringUtils.isNotEmpty(roleIds)) {
            userRoleService.insertByRoleList(sysUser.getUserId(), sysUser.getRoleIds());
        }
        userManager.loadOneUserRedisCache(sysUser.getUserId());
        return ResultVo.oK();
    }

    /**
     * 重置密码
     *
     * @param sysUser
     * @return
     */
    @PutMapping("/resetPwd")
    @RequiresPermissions("system:user:resetPwd")
    @Log(module = "用户管理", operation = "重置用户密码")
    public ResultVo reSetPwd(@RequestBody SysUser sysUser) {
        sysUser.setPassword(MD5Util.encrypt(sysUser.getUsername(), sysUser.getPassword()));
        userService.updateById(sysUser);
        return ResultVo.oK();
    }


    /**
     * 个人中心配置
     */
    @GetMapping("/profile")
    public ResultVo profile(HttpServletRequest request) {

        Integer userId = JWTUtil.getUserId(request);
        return ResultVo.oK(userManager.getUser(userId));
    }

    /**
     * 个人中心配置
     */
    @PutMapping("/profile")
    public ResultVo updateProfile(@RequestBody SysUser sysUser) {
        userService.updateById(sysUser);
        // 刷新缓存
        userManager.saveUser(sysUser.getUserId());
        return ResultVo.oK();
    }

    @PutMapping("/profile/updatePwd")
    public ResultVo profileUpdatePwd(QueryRequest queryRequest, HttpServletRequest request) {
        String username = JWTUtil.getUsername(request);
        Integer userId = JWTUtil.getUserId(request);
        SysUser user = userService.getById(userId);
        if (user.getPassword().equalsIgnoreCase(MD5Util.encrypt(username, queryRequest.getOldPassword()))) {
            SysUser sysUser = new SysUser();
            sysUser.setUserId(userId);
            sysUser.setPassword(MD5Util.encrypt(username, queryRequest.getNewPassword()));
            userService.updateById(sysUser);
            return ResultVo.oK();
        } else {
            return ResultVo.failed(201, "原密码不正确");
        }
    }

    @PostMapping("/profile/avatar")
    public ResultVo avatarUpload(@RequestParam("avatarfile") MultipartFile file, HttpServletRequest request) {
        Integer userId = JWTUtil.getUserId(request);
        JSONObject result = UploadFileUtils.upload(file);
        if (result.getInteger("status") == 200) {
            if (result.getBoolean("isImage")) {
                SysUser user = new SysUser();
                user.setAvatar(result.getString("requestUrl"));
                user.setUserId(userId);
                userService.updateById(user);
                // 缓存
                userManager.saveUser(userId);
                return ResultVo.oK(result.getString("requestUrl"));
            } else {
                return ResultVo.failed(201, "不是图片文件");
            }
        } else {
            return ResultVo.failed(201, "文件上传失败,请联系管理员");
        }

    }
}
