package com.hxxzt.recruitment.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxxzt.recruitment.business.entity.Click;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

import java.util.List;

/**
 * <p>
 * 点赞表 服务类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
public interface IClickService extends IService<Click> {

    IPage<Click> queryPageByPojo(QueryRequest request, Click click);

    List<Click> selectListByWxUserId(Integer wxUserId);

    Integer selectClickCountByStudent(Integer id, Integer type);

    Integer selectClickCountByNotStudent(Integer type);
}
