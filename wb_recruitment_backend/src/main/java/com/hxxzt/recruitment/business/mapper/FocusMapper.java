package com.hxxzt.recruitment.business.mapper;

import com.hxxzt.recruitment.business.entity.Focus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 关注表 Mapper 接口
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
public interface FocusMapper extends BaseMapper<Focus> {

    List<Focus> selectListByWxUserId(@Param("wxUserId") Integer wxUserId);

    Integer selectFocusCountByStudent(@Param("id") Integer id, @Param("type") Integer type);

    Integer selectFocusCountByNotStudent(@Param("type") Integer type);
}
