package com.hxxzt.recruitment.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.business.entity.CareerTalk;
import com.hxxzt.recruitment.business.mapper.CareerTalkMapper;
import com.hxxzt.recruitment.business.service.ICareerTalkService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * <p>
 * 宣讲会表 服务实现类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@Service
public class CareerTalkServiceImpl extends ServiceImpl<CareerTalkMapper, CareerTalk> implements ICareerTalkService {

    @Resource
    private CareerTalkMapper careerTalkMapper;

    @Override
    public IPage<CareerTalk> queryPageByPojo(QueryRequest request, CareerTalk careerTalk) {
        IPage<CareerTalk> careerTalkIPage = new Page<>(request.getPageNum(), request.getPageSize(), true);
        LambdaQueryWrapper<CareerTalk> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(careerTalk.getUsername())) {
            lambdaQueryWrapper.like(CareerTalk::getUsername, careerTalk.getUsername());
        }
        if (!StringUtils.isEmpty(request.getBeginTime()) && !StringUtils.isEmpty(request.getEndTime())) {
            lambdaQueryWrapper.between(CareerTalk::getCreateTime, request.getBeginTime(), request.getEndTime());
        }
        if (!StringUtils.isEmpty(careerTalk.getUserId())) {
            lambdaQueryWrapper.eq(CareerTalk::getUserId, careerTalk.getUserId());
        }
        lambdaQueryWrapper.orderByDesc(CareerTalk::getCreateTime);
        return careerTalkMapper.selectPage(careerTalkIPage, lambdaQueryWrapper);
    }
}
