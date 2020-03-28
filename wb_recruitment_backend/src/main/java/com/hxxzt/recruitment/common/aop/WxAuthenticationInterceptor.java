package com.hxxzt.recruitment.common.aop;

import com.hxxzt.recruitment.business.entity.WxUser;
import com.hxxzt.recruitment.business.service.IWxUserService;
import com.hxxzt.recruitment.common.aop.annotation.PassToken;
import com.hxxzt.recruitment.common.aop.annotation.WxLoginToken;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.exception.PenintTokenException;
import com.hxxzt.recruitment.common.properties.PenintProperties;
import com.hxxzt.recruitment.common.utils.Constant;
import com.hxxzt.recruitment.common.utils.IPUtil;
import com.hxxzt.recruitment.common.utils.PenintUtil;
import com.hxxzt.recruitment.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class WxAuthenticationInterceptor implements HandlerInterceptor {


    @Autowired
    private IWxUserService wxUserService;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private PenintProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws PenintTokenException {
        String encryptToken = httpServletRequest.getHeader("WXAuthorization");// 从 http 请求头中取出 token

        String token = PenintUtil.decryptToken(encryptToken);

        String ip = IPUtil.getIpAddr(httpServletRequest);
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(WxLoginToken.class)) {
            WxLoginToken wxLoginToken = method.getAnnotation(WxLoginToken.class);
            if (wxLoginToken.required()) {
                // 执行认证
                if (encryptToken == null) {
                    throw new PenintTokenException("未认证，请在前端系统进行认证");
                }
                // 获取 token 中的 userId
                String openId = JWTUtil.getWxOpenId(token);
                WxUser wxUser = wxUserService.selectByOpenId(openId);

                if (wxUser == null) {
                    throw new PenintTokenException("用户不存在，请重新登录");
                }
                // 验证 token
                boolean verify = JWTUtil.verifyWx(token, wxUser.getOpenId(), Constant.RM_WX_SECRET);
                if (!verify) {
                    throw new PenintTokenException("验证token失败");
                } else {
                    // 验证redis中的token
                    if (redisUtil.hasKey(Constant.RM_WX_TOKEN_CACHE + openId)) {
                        String redisToken = redisUtil.get(Constant.RM_WX_TOKEN_CACHE + openId);
                        if (!redisToken.equalsIgnoreCase(encryptToken)) {
                            throw new PenintTokenException("验证token失败");
                        }
                        // 刷新redis中的缓存
                        redisUtil.set(Constant.RM_WX_TOKEN_CACHE + openId, encryptToken, properties.getShiro().getWxTokenTimeOut());
                        return true;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}