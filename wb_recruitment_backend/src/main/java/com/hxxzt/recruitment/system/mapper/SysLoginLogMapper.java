package com.hxxzt.recruitment.system.mapper;

import com.hxxzt.recruitment.system.entity.SysLoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 系统登录日志 Mapper 接口
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    void clean();
}
