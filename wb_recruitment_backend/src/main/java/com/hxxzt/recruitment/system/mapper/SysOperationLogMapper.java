package com.hxxzt.recruitment.system.mapper;

import com.hxxzt.recruitment.system.entity.SysOperationLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 操作日志表 Mapper 接口
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {

    void clean();

    List<SysOperationLog> getModule();
}
