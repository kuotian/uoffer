package com.hxxzt.recruitment.common.manager;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hxxzt.recruitment.common.exception.PenintException;
import com.hxxzt.recruitment.common.exception.RedisConnectException;
import com.hxxzt.recruitment.common.utils.Constant;
import com.hxxzt.recruitment.common.utils.RedisUtil;
import com.hxxzt.recruitment.system.entity.SysMenu;
import com.hxxzt.recruitment.system.entity.SysRole;
import com.hxxzt.recruitment.system.entity.SysUser;
import com.hxxzt.recruitment.system.entity.SysUserConfig;
import com.hxxzt.recruitment.system.service.ISysMenuService;
import com.hxxzt.recruitment.system.service.ISysRoleService;
import com.hxxzt.recruitment.system.service.ISysUserConfigService;
import com.hxxzt.recruitment.system.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CacheManager {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysUserConfigService userConfigService;

    @Autowired
    private ObjectMapper mapper;

    /**
     * 测试 Redis是否连接成功
     */
    public void testConnect() throws RedisConnectException {
        redisUtil.get("test");
    }

    /**
     * 从缓存中获取用户
     *
     * @param userId 用户Id
     * @return User
     */
    public SysUser getUser(Integer userId) throws PenintException {
        if (redisUtil.hasKey(Constant.USER_USERINFO_CACHE_PREFIX + userId)) {
            String userString = redisUtil.get(Constant.USER_USERINFO_CACHE_PREFIX + userId);
            if (StringUtils.isBlank(userString)) {
                throw new PenintException("");
            } else {
                try {
                    return this.mapper.readValue(userString, SysUser.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 从缓存中获取用户角色
     *
     * @param userId 用户Id
     * @return 角色集
     */
    public List<SysRole> getRoles(Integer userId) throws Exception {
        if (redisUtil.hasKey(Constant.USER_ROLE_CACHE_PREFIX + userId)) {
            String roleListString = redisUtil.get(Constant.USER_ROLE_CACHE_PREFIX + userId);
            if (StringUtils.isBlank(roleListString)) {
                throw new Exception();
            } else {
                JavaType type = mapper.getTypeFactory().constructParametricType(List.class, SysRole.class);
                return this.mapper.readValue(roleListString, type);
            }
        }
        return null;
    }


    /**
     * 从缓存中获取用户权限
     *
     * @param userId 用户Id
     * @return 权限集
     */
    public List<SysMenu> getPermissions(Integer userId) throws Exception {
        if (redisUtil.hasKey(Constant.USER_PERMISSION_CACHE_PREFIX + userId)) {
            String permissionListString = redisUtil.get(Constant.USER_PERMISSION_CACHE_PREFIX + userId);
            if (StringUtils.isBlank(permissionListString)) {
                throw new Exception();
            } else {
                JavaType type = mapper.getTypeFactory().constructParametricType(List.class, SysMenu.class);
                return this.mapper.readValue(permissionListString, type);
            }
        }
        return null;
    }

    /**
     * 从缓存中获取用户个性化配置
     *
     * @param userId 用户 ID
     * @return 个性化配置
     */
    public SysUserConfig getUserConfig(Integer userId) throws Exception {

        if (redisUtil.hasKey(Constant.USER_CONFIG_CACHE_PREFIX + userId)) {
            String userConfigString = redisUtil.get(Constant.USER_CONFIG_CACHE_PREFIX + userId);

            if (StringUtils.isBlank(userConfigString)) {
                throw new Exception();
            } else {
                return this.mapper.readValue(userConfigString, SysUserConfig.class);
            }
        }
        return null;
    }


    /**
     * 缓存用户信息，只有当用户信息是查询出来的，完整的，才应该调用这个方法
     * 否则需要调用下面这个重载方法
     *
     * @param userId 用户Id
     */
    public void saveUser(Integer userId) {
        deleteUserInfo(userId);
        try {
            redisUtil.set(Constant.USER_USERINFO_CACHE_PREFIX + userId, mapper.writeValueAsString(this.userService.getInfoById(userId)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存用户信息
     *
     * @param userId 用户Id
     */
    public void saveUserInfo(Integer userId) throws PenintException {
        deleteUserInfo(userId);
        SysUser user = this.userService.getInfoById(userId);
        this.deleteUserInfo(userId);
        try {
            redisUtil.set(Constant.USER_USERINFO_CACHE_PREFIX + userId, mapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存用户角色信息
     *
     * @param userId 用户Id
     */
    public void saveRoles(Integer userId) {
        deleteRoles(userId);
        List<SysRole> roleList = this.roleService.findUserRoleByUserId(userId);
        if (!roleList.isEmpty()) {
            this.deleteRoles(userId);
            try {
                redisUtil.set(Constant.USER_ROLE_CACHE_PREFIX + userId, mapper.writeValueAsString(roleList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 缓存用户权限信息
     *
     * @param userId 用户Id
     */
    public void savePermissions(Integer userId) {
        deletePermissions(userId);
        List<SysMenu> permissionList = this.menuService.findUserPermissions(userId);
        deletePermissions(userId);
        if (!permissionList.isEmpty()) {
//            this.deletePermissions(userId);

            redisUtil.set(Constant.USER_PERMISSION_CACHE_PREFIX + userId, JSONArray.toJSONString(permissionList));

        }
    }

    /**
     * 缓存用户个性化配置
     *
     * @param userId 用户 ID
     */
    public void saveUserConfigs(Integer userId) {
        deleteUserConfigs(userId);
        LambdaQueryWrapper<SysUserConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserConfig::getUserId, userId);
        SysUserConfig userConfig = this.userConfigService.getOne(lambdaQueryWrapper);
        if (userConfig != null) {
            this.deleteUserConfigs(userId);
            try {
                redisUtil.set(Constant.USER_CONFIG_CACHE_PREFIX + userId, mapper.writeValueAsString(userConfig));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除用户缓存信息
     *
     * @param userId 用户Id
     */
    public void deleteUserInfo(Integer userId) {
        redisUtil.del(Constant.USER_USERINFO_CACHE_PREFIX + userId);
    }

    /**
     * 删除用户角色信息
     *
     * @param userId 用户Id
     */
    public void deleteRoles(Integer userId) {
        redisUtil.del(Constant.USER_ROLE_CACHE_PREFIX + userId);
    }

    /**
     * 删除用户权限信息
     *
     * @param userId 用户Id
     */
    public void deletePermissions(Integer userId) {
        redisUtil.del(Constant.USER_PERMISSION_CACHE_PREFIX + userId);
    }

    /**
     * 删除用户个性化配置
     *
     * @param userId 用户 ID
     */
    public void deleteUserConfigs(Integer userId) {
        redisUtil.del(Constant.USER_CONFIG_CACHE_PREFIX + userId);
    }

}