package com.hxxzt.recruitment.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.business.entity.ResumeSend;
import com.hxxzt.recruitment.business.mapper.ResumeSendMapper;
import com.hxxzt.recruitment.business.service.IResumeSendService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 简历投递表 服务实现类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-05
 */
@Service
public class ResumeSendServiceImpl extends ServiceImpl<ResumeSendMapper, ResumeSend> implements IResumeSendService {

    @Resource
    private ResumeSendMapper resumeSendMapper;

    @Override
    public IPage<ResumeSend> selectByEnterprise(QueryRequest request, String name, String major, Integer userId) {
        IPage<ResumeSend> iPage = new Page<>(request.getPageNum(), request.getPageSize(), true);
        return resumeSendMapper.selectByEnterprise(iPage, request, name, major, userId);
    }

    @Override
    public IPage<ResumeSend> select(QueryRequest request, String name, String major) {
        IPage<ResumeSend> iPage = new Page<>(request.getPageNum(), request.getPageSize(), true);
        return resumeSendMapper.select(iPage, request, name, major);
    }

    @Override
    public void tagCheck(Integer resumeSendId) {
        ResumeSend resumeSend = new ResumeSend();
        resumeSend.setId(resumeSendId);
        resumeSend.setIsCheck(true);
        resumeSend.setCheckTime(new Date());
        resumeSendMapper.updateById(resumeSend);
    }

    @Override
    public List<ResumeSend> queryList(Integer wxUserId) {
        return resumeSendMapper.queryList(wxUserId);
    }

    @Override
    public List<ResumeSend> queryListByIsCheck(Integer wxUserId, Boolean isCheck) {
        return resumeSendMapper.queryListByIsCheck(wxUserId, isCheck);
    }

    @Override
    public List<ResumeSend> queryListByStatus(Integer wxUserId, Integer status) {
        return resumeSendMapper.queryListByStatus(wxUserId, status);
    }

    @Override
    public ResumeSend isSend(Integer wxUserId, Integer infoId) {
        return resumeSendMapper.isSend(wxUserId, infoId);
    }

}
