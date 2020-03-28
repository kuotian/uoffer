package com.hxxzt.recruitment.business.mapper;

import com.hxxzt.recruitment.business.entity.WxUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 微信小程序用户 Mapper 接口
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-02
 */
public interface WxUserMapper extends BaseMapper<WxUser> {

    List<WxUser> getChart();
}
