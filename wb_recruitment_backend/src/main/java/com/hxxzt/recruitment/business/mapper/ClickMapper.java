package com.hxxzt.recruitment.business.mapper;

import com.hxxzt.recruitment.business.entity.Click;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 点赞表 Mapper 接口
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
public interface ClickMapper extends BaseMapper<Click> {

    List<Click> selectListByWxUserId(@Param("wxUserId") Integer wxUserId);

    Integer selectClickCountByStudent(@Param("id") Integer id, @Param("type") Integer type);

    Integer selectClickCountByNotStudent(Integer type);
}
