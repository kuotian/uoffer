package com.hxxzt.recruitment.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.business.entity.Click;
import com.hxxzt.recruitment.business.mapper.ClickMapper;
import com.hxxzt.recruitment.business.service.IClickService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 点赞表 服务实现类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@Service
public class ClickServiceImpl extends ServiceImpl<ClickMapper, Click> implements IClickService {

    @Resource
    private ClickMapper clickMapper;

    @Override
    public IPage<Click> queryPageByPojo(QueryRequest request, Click click) {
        IPage<Click> clickIPage = new Page<>(request.getPageNum(), request.getPageSize(), true);
        LambdaQueryWrapper<Click> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(request.getBeginTime()) && !StringUtils.isEmpty(request.getEndTime())) {
            lambdaQueryWrapper.between(Click::getCreateTime, request.getBeginTime(), request.getEndTime());
        }
        if (!StringUtils.isEmpty(click.getType())) {
            lambdaQueryWrapper.eq(Click::getType, click.getType());
        }
        lambdaQueryWrapper.orderByDesc(Click::getCreateTime);
        return clickMapper.selectPage(clickIPage, lambdaQueryWrapper);
    }

    @Override
    public List<Click> selectListByWxUserId(Integer wxUserId) {
        return clickMapper.selectListByWxUserId(wxUserId);
    }

    @Override
    public Integer selectClickCountByStudent(Integer id, Integer type) {
        return clickMapper.selectClickCountByStudent(id, type);
    }

    @Override
    public Integer selectClickCountByNotStudent(Integer type) {
        return clickMapper.selectClickCountByNotStudent(type);
    }
}
