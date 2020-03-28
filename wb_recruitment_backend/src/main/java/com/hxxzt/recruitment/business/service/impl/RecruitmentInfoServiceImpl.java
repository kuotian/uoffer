package com.hxxzt.recruitment.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.business.entity.RecruitmentInfo;
import com.hxxzt.recruitment.business.mapper.RecruitmentInfoMapper;
import com.hxxzt.recruitment.business.service.IRecruitmentInfoService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * <p>
 * 招聘信息 服务实现类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@Service
public class RecruitmentInfoServiceImpl extends ServiceImpl<RecruitmentInfoMapper, RecruitmentInfo> implements IRecruitmentInfoService {

    @Resource
    private RecruitmentInfoMapper recruitmentInfoMapper;

    @Override
    public IPage<RecruitmentInfo> queryPageByPojo(QueryRequest request, RecruitmentInfo recruitmentInfo) {
        IPage<RecruitmentInfo> recruitmentInfoIPage = new Page<>(request.getPageNum(), request.getPageSize(), true);
        LambdaQueryWrapper<RecruitmentInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(recruitmentInfo.getUserId())) {
            lambdaQueryWrapper.eq(RecruitmentInfo::getUserId, recruitmentInfo.getUserId());
        }
        if (!StringUtils.isEmpty(recruitmentInfo.getUsername())) {
            lambdaQueryWrapper.like(RecruitmentInfo::getUsername, recruitmentInfo.getUsername());
        }
        if (!StringUtils.isEmpty(request.getBeginTime()) && !StringUtils.isEmpty(request.getEndTime())) {
            lambdaQueryWrapper.between(RecruitmentInfo::getCreateTime, request.getBeginTime(), request.getEndTime());
        }
        lambdaQueryWrapper.orderByDesc(RecruitmentInfo::getCreateTime);
        return recruitmentInfoMapper.selectPage(recruitmentInfoIPage, lambdaQueryWrapper);
    }

    @Override
    public RecruitmentInfo queryListByRsId(Integer rsId) {
        return recruitmentInfoMapper.queryListByRsId(rsId);
    }
}
