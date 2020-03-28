package com.hxxzt.recruitment.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.system.entity.SysLoginLog;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

import java.util.List;

/**
 * <p>
 * 系统登录日志 服务类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
public interface ISysLoginLogService extends IService<SysLoginLog> {

    IPage<SysLoginLog> queryList(QueryRequest queryRequest, SysLoginLog loginLog);

    void clean();

    List<SysLoginLog> selectAll();
}
