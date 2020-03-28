package com.hxxzt.recruitment.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.business.entity.Resume;
import com.hxxzt.recruitment.business.mapper.ResumeMapper;
import com.hxxzt.recruitment.business.service.IResumeService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 简历表 服务实现类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-05
 */
@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements IResumeService {

    @Resource
    private ResumeMapper resumeMapper;

    @Override
    public IPage<Resume> queryPageByPojo(QueryRequest request, Resume resume) {
        IPage<Resume> resumeIPage = new Page<>(request.getPageNum(), request.getPageSize(), true);
        LambdaQueryWrapper<Resume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(resume.getName())) {
            lambdaQueryWrapper.like(Resume::getName, resume.getName());
        }
        if (!StringUtils.isEmpty(resume.getMajor())) {
            lambdaQueryWrapper.like(Resume::getMajor, resume.getMajor());
        }
        if (!StringUtils.isEmpty(request.getBeginTime()) && !org.springframework.util.StringUtils.isEmpty(request.getEndTime())) {
            lambdaQueryWrapper.between(Resume::getCreateTime, request.getBeginTime(), request.getEndTime());
        }
        lambdaQueryWrapper.orderByDesc(Resume::getCreateTime);
        return resumeMapper.selectPage(resumeIPage, lambdaQueryWrapper);
    }

    @Override
    public Resume getResumeByResumeSendId(Integer resumeSendId) {
        return resumeMapper.getResumeByResumeSendId(resumeSendId);
    }

    @Override
    public List<Resume> getByWxUserId(Integer wxUserId) {
        LambdaQueryWrapper<Resume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Resume::getWxUserId, wxUserId).orderByDesc(Resume::getCreateTime);
        return resumeMapper.selectList(lambdaQueryWrapper);
    }
}
