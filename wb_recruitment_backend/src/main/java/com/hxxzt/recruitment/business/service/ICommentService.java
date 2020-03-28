package com.hxxzt.recruitment.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxxzt.recruitment.business.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
public interface ICommentService extends IService<Comment> {

    IPage<Comment> queryPageByPojo(QueryRequest request, Comment comment);

    List<Comment> selectListByWxUserId(Integer wxUserId);

    List<Comment> selectCommonByStudent(Integer id, Integer type);

    List<Comment> selectCommonByNotStudent(Integer type);
}
