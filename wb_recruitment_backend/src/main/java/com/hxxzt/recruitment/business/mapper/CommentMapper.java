package com.hxxzt.recruitment.business.mapper;

import com.hxxzt.recruitment.business.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectListByWxUserId(@Param("wxUserId") Integer wxUserId);

    List<Comment> selectCommonByStudent(@Param("id") Integer id, @Param("type") Integer type);

    List<Comment> selectCommonByNotStudent(@Param("type") Integer type);
}
