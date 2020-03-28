package com.hxxzt.recruitment.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.business.entity.Focus;
import com.hxxzt.recruitment.business.mapper.FocusMapper;
import com.hxxzt.recruitment.business.service.IFocusService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 关注表 服务实现类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@Service
public class FocusServiceImpl extends ServiceImpl<FocusMapper, Focus> implements IFocusService {

    @Resource
    private FocusMapper focusMapper;

    @Override
    public IPage<Focus> queryPageByPojo(QueryRequest request, Focus focus) {
        IPage<Focus> focusIPage = new Page<>(request.getPageNum(), request.getPageSize(), true);
        LambdaQueryWrapper<Focus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(request.getBeginTime()) && !StringUtils.isEmpty(request.getEndTime())) {
            lambdaQueryWrapper.between(Focus::getCreateTime, request.getBeginTime(), request.getEndTime());
        }
        if (!StringUtils.isEmpty(focus.getType())) {
            lambdaQueryWrapper.eq(Focus::getType, focus.getType());
        }
        lambdaQueryWrapper.orderByDesc(Focus::getCreateTime);
        return focusMapper.selectPage(focusIPage, lambdaQueryWrapper);
    }

    @Override
    public List<Focus> selectListByWxUserId(Integer wxUserId) {
        return focusMapper.selectListByWxUserId(wxUserId);
    }

    @Override
    public Integer selectFocusCountByStudent(Integer id, Integer type) {
        return focusMapper.selectFocusCountByStudent(id, type);
    }

    @Override
    public Integer selectFocusCountByNotStudent(Integer type) {
        return focusMapper.selectFocusCountByNotStudent(type);
    }
}
