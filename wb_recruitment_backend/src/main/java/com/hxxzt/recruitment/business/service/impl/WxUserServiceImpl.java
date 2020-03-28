package com.hxxzt.recruitment.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.business.entity.WxUser;
import com.hxxzt.recruitment.business.mapper.WxUserMapper;
import com.hxxzt.recruitment.business.service.IWxUserService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 微信小程序用户 服务实现类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-02
 */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements IWxUserService {

    @Resource
    private WxUserMapper wxUserMapper;

    @Override
    public WxUser selectByOpenId(String openid) {
        LambdaQueryWrapper<WxUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WxUser::getOpenId, openid);
        return wxUserMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public IPage<WxUser> queryPageByPojo(QueryRequest request, WxUser wxUser) {
        IPage<WxUser> wxUserIPage = new Page<>(request.getPageNum(), request.getPageSize(), true);
        LambdaQueryWrapper<WxUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(request.getBeginTime()) && !StringUtils.isEmpty(request.getEndTime())) {
            lambdaQueryWrapper.between(WxUser::getCreateTime, request.getBeginTime(), request.getEndTime());
        }
        if (!StringUtils.isEmpty(wxUser.getNickName())) {
            lambdaQueryWrapper.like(WxUser::getNickName, wxUser.getNickName());
        }
        lambdaQueryWrapper.orderByDesc(WxUser::getCreateTime);
        return wxUserMapper.selectPage(wxUserIPage, lambdaQueryWrapper);
    }

    @Override
    public List<WxUser> getChart() {
        return wxUserMapper.getChart();
    }
}
