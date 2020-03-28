package com.hxxzt.recruitment.business.service;

import com.alibaba.fastjson.JSONObject;
import com.hxxzt.recruitment.business.entity.Todo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 待办事项 服务类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-27
 */
public interface ITodoService extends IService<Todo> {

    JSONObject selectList(Integer id);

    List<Todo> selectListByTime(Integer id, String time);
}
