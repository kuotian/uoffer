package com.hxxzt.recruitment.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxxzt.recruitment.business.entity.CareerTalk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

/**
 * <p>
 * 宣讲会表 服务类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
public interface ICareerTalkService extends IService<CareerTalk> {

    IPage<CareerTalk> queryPageByPojo(QueryRequest request, CareerTalk careerTalk);
}
