package com.hxxzt.recruitment.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxxzt.recruitment.business.entity.Enterprise;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

/**
 * <p>
 * 企业信息表 服务类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
public interface IEnterpriseService extends IService<Enterprise> {

    IPage<Enterprise> queryPageByPojo(QueryRequest request, Enterprise enterprise);

    Enterprise queryByUserId(Integer userId);
}
