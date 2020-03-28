package com.hxxzt.recruitment.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.business.entity.Enterprise;
import com.hxxzt.recruitment.business.mapper.EnterpriseMapper;
import com.hxxzt.recruitment.business.service.IEnterpriseService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 企业信息表 服务实现类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements IEnterpriseService {

    @Resource
    private EnterpriseMapper enterpriseMapper;

    @Override
    public IPage<Enterprise> queryPageByPojo(QueryRequest request, Enterprise enterprise) {
        IPage<Enterprise> enterpriseIPage = new Page<>(request.getPageNum(), request.getPageSize(), true);
        LambdaQueryWrapper<Enterprise> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(enterprise.getUsername())) {
            lambdaQueryWrapper.like(Enterprise::getUsername, enterprise.getUsername());
        }
        if (StringUtils.isNotEmpty(request.getBeginTime()) && StringUtils.isNotEmpty(request.getEndTime())) {
            lambdaQueryWrapper.between(Enterprise::getCreateTime, request.getBeginTime(), request.getEndTime());
        }
        if (enterprise.getReviewStatus() != null) {
            lambdaQueryWrapper.eq(Enterprise::getReviewStatus, enterprise.getReviewStatus());
        }
        lambdaQueryWrapper.orderByDesc(Enterprise::getCreateTime);
        return enterpriseMapper.selectPage(enterpriseIPage, lambdaQueryWrapper);
    }

    @Override
    public Enterprise queryByUserId(Integer userId) {
        LambdaQueryWrapper<Enterprise> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Enterprise::getUserId, userId);
        return enterpriseMapper.selectOne(lambdaQueryWrapper);
    }
}
