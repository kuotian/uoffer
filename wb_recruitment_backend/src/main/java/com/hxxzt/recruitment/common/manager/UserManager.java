package com.hxxzt.recruitment.common.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxxzt.recruitment.business.entity.Enterprise;
import com.hxxzt.recruitment.business.service.IEnterpriseService;
import com.hxxzt.recruitment.common.utils.PenintUtil;
import com.hxxzt.recruitment.system.entity.*;
import com.hxxzt.recruitment.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 封装一些和 User相关的业务操作
 */
@Service
public class UserManager {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    @Lazy
    private ISysRoleService roleService;
    @Autowired
    @Lazy
    private ISysMenuService menuService;
    @Autowired
    @Lazy
    private ISysUserService userService;
    @Autowired
    private ISysUserConfigService userConfigService;
    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private IEnterpriseService enterpriseService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    /**
     * 通过用户名获取用户基本信息
     *
     * @param username 用户名
     * @return 用户基本信息
     */
    public SysUser getUser(String username) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUsername, username);
        return this.userService.getOne(lambdaQueryWrapper);
    }

    /**
     * 通过用户Id获取用户基本信息
     *
     * @return 用户基本信息
     */
    public SysUser getUser(Integer userId) {
        return PenintUtil.selectCacheByTemplate(
                () -> this.cacheManager.getUser(userId),
                () -> this.userService.getInfoById(userId));
    }

    public void saveUser(Integer userId) {
        cacheManager.saveUser(userId);
    }


    /**
     * 通过用户Id获取用户角色集合
     *
     * @param userId 用户Id
     * @return 角色集合
     */
    public Set<String> getUserRoles(Integer userId) {
        List<SysRole> roleList = PenintUtil.selectCacheByTemplate(
                () -> this.cacheManager.getRoles(userId),
                () -> this.roleService.findUserRoleByUserId(userId));
        return roleList.stream().map(SysRole::getRoleName).collect(Collectors.toSet());
    }

    /**
     * TODO 可用
     * 通过用户Id获取用户权限集合
     *
     * @param userId 用户Id
     * @return 权限集合
     */
    public Set<String> getUserPermissions(Integer userId) {
        List<SysMenu> permissionList = PenintUtil.selectCacheByTemplate(
                () -> cacheManager.getPermissions(userId),
                () -> menuService.findUserPermissions(userId));
        return permissionList.stream().map(SysMenu::getPerms).collect(Collectors.toSet());
    }


    /**
     * 通过用户 ID获取前端系统个性化配置
     *
     * @param userId 用户 ID
     * @return 前端系统个性化配置
     */
    public SysUserConfig getUserConfig(Integer userId) throws Exception {
        LambdaQueryWrapper<SysUserConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserConfig::getUserId, userId);

        return PenintUtil.selectCacheByTemplate(
                () -> cacheManager.getUserConfig(userId),
                () -> userConfigService.getOne(lambdaQueryWrapper));
    }

    /**
     * 将用户相关信息添加到 Redis缓存中
     *
     * @param userId 用户Id
     */
    @Async
    public void loadOneUserRedisCache(Integer userId) {
        // 缓存用户
        cacheManager.saveUser(userId);
        // 缓存用户角色
        cacheManager.saveRoles(userId);
        // 缓存用户权限
        cacheManager.savePermissions(userId);
        // 缓存用户个性化配置
//        cacheManager.saveUserConfigs(userId);
    }

    /**
     * 将用户角色和权限添加到 Redis缓存中
     *
     * @param userIds userIds
     */
    public void loadBatchUserRedisCache(List<Integer> userIds) {
        for (Integer userId : userIds) {
            // 缓存用户角色
            cacheManager.saveUser(userId);
            // 缓存用户权限
            cacheManager.savePermissions(userId);
            // 缓存角色权限
            cacheManager.saveRoles(userId);
        }
    }

    /**
     * 通过用户id集合批量删除用户 Redis缓存
     *
     * @param userIds userIds
     */
    public void deleteBatchUserRedisCache(List<Integer> userIds) {
        for (Integer userId : userIds) {
            cacheManager.deleteUserInfo(userId);
            cacheManager.deleteRoles(userId);
            cacheManager.deletePermissions(userId);
//            cacheService.deleteUserConfigs(userId);
        }
    }

    public void deleteUserRedisCache(Integer userId) {
        cacheManager.deleteUserInfo(userId);
        cacheManager.deleteRoles(userId);
        cacheManager.deletePermissions(userId);
//            cacheService.deleteUserConfigs(userId);
    }


    /**
     * 根据UserId 判断是不是超级管理员(admin)
     */
    public boolean isAdmin(Integer userId) {
        SysUser user = userService.getById(userId);
        if (user.getUsername().equalsIgnoreCase("admin")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断后台管理员是否禁止发布招聘及宣讲会信息
     *
     * @return true 可以发布
     * false 禁止发布
     */
    public boolean isPublic(Integer userId) {
        LambdaQueryWrapper<Enterprise> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Enterprise::getUserId, userId);
        Enterprise enterprise = enterpriseService.getOne(lambdaQueryWrapper);
        if (enterprise.getIsPublish()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询用户是否为企业用户
     * 企业用户roleId为72
     */
    public boolean isEnterpriseRole(Integer userId) {
        LambdaQueryWrapper<SysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> list = sysUserRoleService.list(lambdaQueryWrapper);
        for (SysUserRole userRole : list) {
            if (userRole.getRoleId() == 72) {
                return true;
            }
        }
        return false;
    }
}
