package com.hxxzt.recruitment.common.utils;

/**
 * 一些常用的常量
 */
public class Constant {

    /**
     * 账户状态：1为启用
     */
    public static final Integer STATUS_VALID = 1;

    /**
     * 账户状态：0为禁用
     */
    public static final Integer STATUS_LOCK = 0;

    // 默认密码
    public static final String DEFAULT_PASSWORD = "1234qwer";

    // 默认头像
    public static final String DEFAULT_AVATAR = "http://47.104.212.248:8080/image/defaultAvatar.jpg";

    // token缓存前缀+加密token+ip
    public static final String RM_TOKEN_CACHE = "RM_ELEMENT_TOKEN_CACHE_";

    // user用户信息前缀+userId
    public static final String USER_USERINFO_CACHE_PREFIX = "RM_ELEMENT_USERINFO_CACHE_";
    // user角色缓存前缀+userId
    public static final String USER_ROLE_CACHE_PREFIX = "RM_ELEMENT_ROLE_CACHE_USER_";
    // user权限缓存前缀+userId
    public static final String USER_PERMISSION_CACHE_PREFIX = "RM_ELEMENT_PERMISSION_CACHE_";
    // user个性化配置前缀+userId
    public static final String USER_CONFIG_CACHE_PREFIX = "RM_ELEMENT_CONFIG_CACHE_USER_";

    /**
     * 验证码 redis key
     */
    public static final String RM_CAPTCHA_CODE_KEY = "rm_captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 缓存在线用户Zset集合
     */
    public static final String RM_ACTIVE_USERS_ZSET_PREFIX = "RM_ACTIVE_USERS_ZSET_PREFIX";

    /**
     * 小程序用户token
     */
    public static final String RM_WX_TOKEN_CACHE = "RM_WX_TOKEN_CACHE_";

    /**
     * 小程序用户token 密钥 secret
     */
    public static final String RM_WX_SECRET = "RM_WX_SECRET_ADJAKmjjaSSA";
}