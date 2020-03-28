package com.hxxzt.recruitment.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxxzt.recruitment.business.entity.Todo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 待办事项 Mapper 接口
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-27
 */
public interface TodoMapper extends BaseMapper<Todo> {

    List<Todo> getToday(@Param("id") Integer id);

    List<Todo> getTomorrow(@Param("id") Integer id);

    List<Todo> selectListByTime(@Param("id") Integer id, @Param("time") String time);
}
