package com.hxxzt.recruitment.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.business.entity.ResumeSend;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

import java.util.List;

/**
 * <p>
 * 简历投递表 服务类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-05
 */
public interface IResumeSendService extends IService<ResumeSend> {

    IPage<ResumeSend> selectByEnterprise(QueryRequest request, String name, String major, Integer userId);

    IPage<ResumeSend> select(QueryRequest request, String name, String major);

    void tagCheck(Integer resumeSendId);

    List<ResumeSend> queryList(Integer wxUserId);

    List<ResumeSend> queryListByIsCheck(Integer wxUserId, Boolean isCheck);

    List<ResumeSend> queryListByStatus(Integer wxUserId, Integer status);

    ResumeSend isSend(Integer wxUserId, Integer infoId);
}
