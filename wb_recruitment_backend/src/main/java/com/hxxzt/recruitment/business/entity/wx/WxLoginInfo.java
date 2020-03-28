package com.hxxzt.recruitment.business.entity.wx;

public class WxLoginInfo {

    /**
     * 微信临时登陆凭证code
     */
    private String code;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}