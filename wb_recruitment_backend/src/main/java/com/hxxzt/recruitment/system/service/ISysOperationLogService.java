package com.hxxzt.recruitment.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hxxzt.recruitment.system.entity.SysOperationLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
public interface ISysOperationLogService extends IService<SysOperationLog> {

    @Async
    void saveLog(ProceedingJoinPoint point, SysOperationLog operationLog) throws JsonProcessingException;

    IPage<SysOperationLog> getList(QueryRequest queryRequest, SysOperationLog operationLog);

    void clean();

    List<SysOperationLog> selectAll();

    List<SysOperationLog> getModule();
}
