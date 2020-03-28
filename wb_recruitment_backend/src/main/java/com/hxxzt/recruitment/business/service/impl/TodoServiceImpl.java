package com.hxxzt.recruitment.business.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hxxzt.recruitment.business.entity.Todo;
import com.hxxzt.recruitment.business.mapper.TodoMapper;
import com.hxxzt.recruitment.business.service.ITodoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.common.vo.ResultVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 待办事项 服务实现类
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-27
 */
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements ITodoService {

    @Resource
    private TodoMapper todoMapper;

    @Override
    public JSONObject selectList(Integer id) {
        List<Todo> today = todoMapper.getToday(id);
        List<Todo> tomorrow = todoMapper.getTomorrow(id);
        JSONObject data = new JSONObject();
        data.put("today", today);
        data.put("tomorrow", tomorrow);
        return data;
    }

    @Override
    public List<Todo> selectListByTime(Integer id, String time) {
        return todoMapper.selectListByTime(id, time);
    }
}
