package com.hxxzt.recruitment.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.system.entity.SysOperationLog;
import com.hxxzt.recruitment.system.mapper.SysOperationLogMapper;
import com.hxxzt.recruitment.system.service.ISysOperationLogService;
import com.hxxzt.recruitment.common.utils.AddressUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements ISysOperationLogService {

    @Autowired
    ObjectMapper objectMapper;

    @Resource
    private SysOperationLogMapper operationLogMapper;

    @Override
    @Transactional
    public void saveLog(ProceedingJoinPoint point, SysOperationLog operationLog) throws JsonProcessingException {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            // 注解上的描述
            operationLog.setOperation(logAnnotation.operation());
            operationLog.setModule(logAnnotation.module());
        }
        // 请求的类名
        String className = point.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();
        operationLog.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = point.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            operationLog.setParams(params.toString());
        }
        operationLog.setCreateTime(new Date());
        operationLog.setAddress(AddressUtil.getCityInfo(operationLog.getIp()));
        // 保存系统日志
        save(operationLog);
    }

    @Override
    public IPage<SysOperationLog> getList(QueryRequest queryRequest, SysOperationLog operationLog) {
        IPage<SysOperationLog> logIPage = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
        LambdaQueryWrapper<SysOperationLog> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(queryRequest.getBeginTime()) && !StringUtils.isEmpty(queryRequest.getEndTime())) {
            logLambdaQueryWrapper.between(true, SysOperationLog::getCreateTime, queryRequest.getBeginTime(), queryRequest.getEndTime());
        }
        if (!StringUtils.isEmpty(operationLog.getModule())) {
            logLambdaQueryWrapper.like(SysOperationLog::getModule, operationLog.getModule());
        }
        if (!StringUtils.isEmpty(operationLog.getUsername())) {
            logLambdaQueryWrapper.like(SysOperationLog::getUsername, operationLog.getUsername());
        }
        if (!StringUtils.isEmpty(operationLog.getIsSuccess())) {
            logLambdaQueryWrapper.eq(SysOperationLog::getIsSuccess, operationLog.getIsSuccess());
        }

        logLambdaQueryWrapper.orderByDesc(SysOperationLog::getCreateTime);
        return operationLogMapper.selectPage(logIPage, logLambdaQueryWrapper);
    }

    @Override
    public void clean() {
        operationLogMapper.clean();
    }

    @Override
    public List<SysOperationLog> selectAll() {
        return operationLogMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<SysOperationLog> getModule() {
        return operationLogMapper.getModule();
    }


    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) throws JsonProcessingException {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                Set set = ((Map) args[i]).keySet();
                List<Object> list = new ArrayList<>();
                List<Object> paramList = new ArrayList<>();
                for (Object key : set) {
                    list.add(((Map) args[i]).get(key));
                    paramList.add(key);
                }
                return handleParams(params, list.toArray(), paramList);
            } else {
                if (args[i] instanceof Serializable) {
                    Class<?> aClass = args[i].getClass();
                    try {
                        aClass.getDeclaredMethod("toString", new Class[]{null});
                        // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                        params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
                    } catch (NoSuchMethodException e) {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
                    }
                } else if (args[i] instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) args[i];
                    params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                } else {
                    params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                }
            }
        }
        return params;
    }
}
