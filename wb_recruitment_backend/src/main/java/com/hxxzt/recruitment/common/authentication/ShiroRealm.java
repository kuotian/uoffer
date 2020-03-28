package com.hxxzt.recruitment.common.authentication;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hxxzt.recruitment.common.manager.UserManager;
import com.hxxzt.recruitment.common.utils.*;
import com.hxxzt.recruitment.system.entity.SysUser;
import com.hxxzt.recruitment.system.service.ISysMenuService;
import com.hxxzt.recruitment.system.service.ISysRoleService;
import com.hxxzt.recruitment.system.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 *
 * @author MrBird
 */
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * `
     * 授权模块，获取用户角色和权限
     *
     * @param token token
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
        Integer userId = JWTUtil.getUserId(token.toString());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色集
        Set<String> roleSet = roleService.selectRolePermissionByUserId(userId);
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限集
        Set<String> permissionSet = menuService.findUserPermissionsByUserId(userId);
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param authenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的，已经经过了解密
        String token = (String) authenticationToken.getCredentials();
        String encryptToken = PenintUtil.encryptToken(token);
        String username = JWTUtil.getUsername(token);
        Integer userId = JWTUtil.getUserId(token);

        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        String encryptTokenInRedis = redisUtil.get(Constant.RM_TOKEN_CACHE + encryptToken + StringPool.UNDERSCORE + ip);
        if (!token.equalsIgnoreCase(PenintUtil.decryptToken(encryptTokenInRedis))) {
            throw new AuthenticationException("token已经过期");
        }

        // 如果找不到，说明已经失效
        if (StringUtils.isBlank(encryptTokenInRedis)) {
            throw new AuthenticationException("token已经过期");
        }

        if (StringUtils.isBlank(username)) {
            throw new AuthenticationException("token校验不通过");
        }

        // 通过用户名查询用户信息
        SysUser user = userService.getById(userId);

        if (user == null) {
            throw new AuthenticationException("用户名或密码错误");
        }
        if (!JWTUtil.verify(token, username, user.getPassword())) {
            throw new AuthenticationException("token校验不通过");
        }
        return new SimpleAuthenticationInfo(token, token, "febs_shiro_realm");
    }
}
