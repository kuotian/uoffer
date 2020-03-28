package com.hxxzt.recruitment.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxxzt.recruitment.business.entity.Focus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

import java.util.List;

/**
 * <p>
 * 关注表 服务类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
public interface IFocusService extends IService<Focus> {

    IPage<Focus> queryPageByPojo(QueryRequest request, Focus focus);

    List<Focus> selectListByWxUserId(Integer wxUserId);

    Integer selectFocusCountByStudent(Integer id, Integer type);

    Integer selectFocusCountByNotStudent(Integer type);
}
