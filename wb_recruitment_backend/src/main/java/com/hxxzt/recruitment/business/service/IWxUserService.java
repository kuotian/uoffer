package com.hxxzt.recruitment.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxxzt.recruitment.business.entity.WxUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

import java.util.List;

/**
 * <p>
 * 微信小程序用户 服务类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-02
 */
public interface IWxUserService extends IService<WxUser> {

    WxUser selectByOpenId(String openid);

    IPage<WxUser> queryPageByPojo(QueryRequest request, WxUser wxUser);

    List<WxUser> getChart();

}
