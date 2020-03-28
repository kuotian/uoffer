package com.hxxzt.recruitment.common.aop;

import com.alibaba.fastjson.JSONObject;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.properties.PenintProperties;
import com.hxxzt.recruitment.common.utils.HttpContextUtil;
import com.hxxzt.recruitment.common.utils.IPUtil;
import com.hxxzt.recruitment.system.entity.SysOperationLog;
import com.hxxzt.recruitment.system.service.ISysOperationLogService;
import com.hxxzt.recruitment.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 操作日志记录处理
 */
@Aspect
@Component
@Slf4j
public class LogAspect {


    @Autowired
    private PenintProperties properties;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysOperationLogService operationLogService;

    // 配置织入点
    @Pointcut("@annotation(com.hxxzt.recruitment.common.aop.annotation.Log)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP 地址
        String ip = IPUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (properties.isOpenAopLog()) {
            // 保存日志
            String token = (String) SecurityUtils.getSubject().getPrincipal();
            String username = "";
            if (StringUtils.isNotBlank(token)) {
                username = JWTUtil.getUsername(token);
            }

            String resultStr = JSONObject.toJSONString(result);
            SysOperationLog operationLog = new SysOperationLog();
            operationLog.setUsername(username);
            operationLog.setIp(ip);
            operationLog.setTime(time);
            operationLog.setResult(resultStr);
            if (JSONObject.parseObject(resultStr).getInteger("status") == 200) {
                operationLog.setIsSuccess(true);
            } else {
                operationLog.setIsSuccess(false);
            }
            operationLogService.saveLog(point, operationLog);
        }
        return result;
    }
}
