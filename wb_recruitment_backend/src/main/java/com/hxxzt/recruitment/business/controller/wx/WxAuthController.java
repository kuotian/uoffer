/**
 * 项目名：recruitment
 * 日  期：2020/3/2
 * 包  名：com.hxxzt.recruitment.business.controller.wx
 *
 * @author： niko_hxx
 */
package com.hxxzt.recruitment.business.controller.wx;

import com.alibaba.fastjson.JSONObject;
import com.hxxzt.recruitment.business.entity.WxUser;
import com.hxxzt.recruitment.business.entity.wx.UserInfo;
import com.hxxzt.recruitment.business.entity.wx.WxLoginInfo;
import com.hxxzt.recruitment.business.service.IWxUserService;
import com.hxxzt.recruitment.common.authentication.JWTToken;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.properties.PenintProperties;
import com.hxxzt.recruitment.common.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wxApi/auth")
@Slf4j
@Api(tags = "wx 登录相关接口")
public class WxAuthController {

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appSecret}")
    private String appSecret;


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PenintProperties properties;

    @Autowired
    private IWxUserService wxUserService;

    @PostMapping("/doLogin")
    @ApiOperation(value = "登录")
    public Object loginByWeixin(@RequestBody WxLoginInfo wxLoginInfo, HttpServletRequest request) {

        String code = wxLoginInfo.getCode();
        UserInfo userInfo = wxLoginInfo.getUserInfo();

        // 校验参数是否为空
        if (code == null || userInfo == null) {
            return ResponseUtil.badArgument();
        }

        String sessionKey = null;
        String openid = null;
        String data = "appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        Map<String, Object> map = new HashMap<String, Object>();
        String res = HttpRequestUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session", data);
        JSONObject json = JSONObject.parseObject(res);
        log.info(json.toJSONString());
        sessionKey = json.getString("session_key");
        openid = json.getString("openid");


        if (sessionKey == null || openid == null) {
            return ResponseUtil.fail();
        }

        // 根据openId查询是否有该用户
        WxUser user = new WxUser();
        Date now = new Date();
        WxUser user1 = wxUserService.selectByOpenId(openid);
        JWTToken jwtToken = null;
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getJwtTimeOut());
        if (user1 == null) {
            log.info("openid:" + openid);
            user.setOpenId(openid);
            user.setAvatar(userInfo.getAvatarUrl());
            user.setNickName(userInfo.getNickName());
            user.setGender(userInfo.getGender());
            user.setLastLoginTime(now);
            user.setLastLoginIp(IPUtil.getIpAddr(request));
            user.setCreateTime(now);

            // 将用户信息存入数据库中
            wxUserService.save(user);

            String sign = JWTUtil.signWx(openid, Constant.RM_WX_SECRET, user.getId());
            String token = PenintUtil.encryptToken(sign);
            String expireTimeStr = DateUtil.formatFullTime(expireTime);
            jwtToken = new JWTToken(token, expireTimeStr);

            // token 存入redis
            redisUtil.set(Constant.RM_WX_TOKEN_CACHE + openid, jwtToken.getToken(), properties.getShiro().getWxTokenTimeOut());

        } else {
            // 已经登录过的用户修改上次登录时间和上次登录Ip
            user1.setLastLoginTime(now);
            user1.setLastLoginIp(IPUtil.getIpAddr(request));
            wxUserService.updateById(user1);
            // token
            String sign = JWTUtil.signWx(openid, Constant.RM_WX_SECRET, user1.getId());
            String token = PenintUtil.encryptToken(sign);
            String expireTimeStr = DateUtil.formatFullTime(expireTime);
            jwtToken = new JWTToken(token, expireTimeStr);

            // token 存入redis
            redisUtil.set(Constant.RM_WX_TOKEN_CACHE + openid, jwtToken.getToken(), properties.getShiro().getWxTokenTimeOut());
        }

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", jwtToken.getToken());
        result.put("tokenExpire", jwtToken.getExipreAt());
        result.put("userInfo", userInfo);
        result.put("openId", openid);
        return ResponseUtil.ok(result);

    }
}