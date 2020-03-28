package com.hxxzt.recruitment.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.system.entity.SysLoginLog;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.system.mapper.SysLoginLogMapper;
import com.hxxzt.recruitment.system.service.ISysLoginLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统登录日志 服务实现类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

    @Resource
    private SysLoginLogMapper loginLogMapper;

    @Override
    public IPage<SysLoginLog> queryList(QueryRequest queryRequest, SysLoginLog loginLog) {
        IPage<SysLoginLog> loginLogIPage = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);

        LambdaQueryWrapper<SysLoginLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(loginLog.getUsername())) {
            lambdaQueryWrapper.like(SysLoginLog::getUsername, loginLog.getUsername());
        }
        if (!StringUtils.isEmpty(loginLog.getAddress())) {
            lambdaQueryWrapper.like(SysLoginLog::getAddress, loginLog.getAddress());
        }

        if (!StringUtils.isEmpty(queryRequest.getBeginTime()) && !StringUtils.isEmpty(queryRequest.getEndTime())) {
            lambdaQueryWrapper.between(true, SysLoginLog::getLoginTime, queryRequest.getBeginTime(), queryRequest.getEndTime());
        }

        lambdaQueryWrapper.orderByDesc(SysLoginLog::getLoginTime);
        return loginLogMapper.selectPage(loginLogIPage, lambdaQueryWrapper);
    }

    @Override
    public void clean() {
        loginLogMapper.clean();
    }

    @Override
    public List<SysLoginLog> selectAll() {
        return loginLogMapper.selectList(new LambdaQueryWrapper<>());
    }
}
