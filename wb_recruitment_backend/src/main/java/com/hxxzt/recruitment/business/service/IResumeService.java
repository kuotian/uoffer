package com.hxxzt.recruitment.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.business.entity.Resume;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

import java.util.List;

/**
 * <p>
 * 简历表 服务类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-05
 */
public interface IResumeService extends IService<Resume> {

    IPage<Resume> queryPageByPojo(QueryRequest request, Resume resume);

    Resume getResumeByResumeSendId(Integer resumeSendId);

    List<Resume> getByWxUserId(Integer wxUserId);
}
