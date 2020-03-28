package com.hxxzt.recruitment.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.system.entity.SysUser;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.system.mapper.SysUserMapper;
import com.hxxzt.recruitment.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser findDetail(String username) {
        return sysUserMapper.findDetail(username);
    }

    @Override
    public IPage<SysUser> queryList(QueryRequest queryRequest, SysUser user) {
        Integer pageNum = queryRequest.getPageNum();
        Integer pageSize = queryRequest.getPageSize();
        String beginTime = queryRequest.getBeginTime();
        String endTime = queryRequest.getEndTime();
        IPage<SysUser> userIPage = new Page<>(pageNum, pageSize, true);
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(beginTime) && !StringUtils.isEmpty(endTime)) {
            lambdaQueryWrapper.between(true, SysUser::getCreateTime, beginTime, endTime);
        }
        return sysUserMapper.selectPage(userIPage, lambdaQueryWrapper);
    }

    @Override
    public IPage<SysUser> queryFuzz(QueryRequest queryRequest, SysUser user) {
        IPage<SysUser> userIPage = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
        return sysUserMapper.queryFuzz(userIPage,queryRequest, user);
    }

    @Override
    public SysUser getInfoById(Integer userId) {
        return sysUserMapper.getInfoById(userId);
    }

    @Override
    public void insertSelective(SysUser sysUser) {
        sysUserMapper.insertSelective(sysUser);
    }
}
