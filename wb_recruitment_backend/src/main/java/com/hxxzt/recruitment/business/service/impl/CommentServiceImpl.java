package com.hxxzt.recruitment.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.business.entity.Comment;
import com.hxxzt.recruitment.business.mapper.CommentMapper;
import com.hxxzt.recruitment.business.service.ICommentService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public IPage<Comment> queryPageByPojo(QueryRequest request, Comment comment) {
        IPage<Comment> commentIPage = new Page<>(request.getPageNum(), request.getPageSize(), true);
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(request.getBeginTime()) && !StringUtils.isEmpty(request.getEndTime())) {
            lambdaQueryWrapper.between(Comment::getCreateTime, request.getBeginTime(), request.getEndTime());
        }
        if (!StringUtils.isEmpty(comment.getType())) {
            lambdaQueryWrapper.eq(Comment::getType, comment.getType());
        }
        lambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        return commentMapper.selectPage(commentIPage, lambdaQueryWrapper);
    }

    @Override
    public List<Comment> selectListByWxUserId(Integer wxUserId) {
        return commentMapper.selectListByWxUserId(wxUserId);
    }

    @Override
    public List<Comment> selectCommonByStudent(Integer id, Integer type) {
        return commentMapper.selectCommonByStudent(id, type);
    }

    @Override
    public List<Comment> selectCommonByNotStudent(Integer type) {
        return commentMapper.selectCommonByNotStudent(type);
    }
}
