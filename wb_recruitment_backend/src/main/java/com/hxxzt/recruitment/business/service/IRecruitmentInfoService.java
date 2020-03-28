package com.hxxzt.recruitment.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxxzt.recruitment.business.entity.RecruitmentInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

/**
 * <p>
 * 招聘信息 服务类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
public interface IRecruitmentInfoService extends IService<RecruitmentInfo> {

    IPage<RecruitmentInfo> queryPageByPojo(QueryRequest request, RecruitmentInfo recruitmentInfo);

    RecruitmentInfo queryListByRsId(Integer rsId);
}
