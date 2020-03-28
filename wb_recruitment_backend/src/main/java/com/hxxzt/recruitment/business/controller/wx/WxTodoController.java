package com.hxxzt.recruitment.business.controller.wx;


import com.alibaba.fastjson.JSONObject;
import com.hxxzt.recruitment.business.entity.Todo;
import com.hxxzt.recruitment.business.entity.WxUser;
import com.hxxzt.recruitment.business.service.ITodoService;
import com.hxxzt.recruitment.business.service.IWxUserService;
import com.hxxzt.recruitment.common.aop.annotation.WxLoginToken;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.utils.DateUtil;
import com.hxxzt.recruitment.common.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 待办事项 前端控制器
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-27
 */
@RestController
@RequestMapping("/wxApi/todo")
@Slf4j
@Api(tags = "待办事项相关接口")
public class WxTodoController {


    @Autowired
    private ITodoService todoService;

    @Autowired
    private IWxUserService wxUserService;

    @WxLoginToken
    @GetMapping
    @ApiOperation(value = "查询今明两天todolist")
    public ResultVo list(HttpServletRequest request) {


        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer id = wxUser.getId();

        JSONObject data = todoService.selectList(id);

        return ResultVo.oK(data);

    }


    @WxLoginToken
    @GetMapping("/{time}")
    @ApiOperation(value = "按照时间查询todolist")
    public ResultVo listByTime(HttpServletRequest request, @PathVariable String time) {


        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer id = wxUser.getId();

        List<Todo> todoList = todoService.selectListByTime(id, time);

        return ResultVo.oK(todoList);
    }


    @WxLoginToken
    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除todolist")
    public ResultVo delById(@PathVariable Integer id) {

        todoService.removeById(id);
        return ResultVo.oK();
    }

    @WxLoginToken
    @PostMapping
    @ApiOperation(value = "新增todolist")
    public ResultVo save(HttpServletRequest request, @RequestBody Todo todo) {
        String time1 = todo.getTime1();
        String time2 = todo.getTime2();
        String time = time1 + " " + time2;


        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer id = wxUser.getId();

        todo.setTime(DateUtil.parseTimeFormattoDate(time));
        todo.setWxNickname(wxUser.getNickName());
        todo.setWxUserId(id);

        todoService.save(todo);
        return ResultVo.oK();
    }

    @WxLoginToken
    @PutMapping
    @ApiOperation(value = "修改todolist")
    public ResultVo upt(HttpServletRequest request, @RequestBody Todo todo) {

        String time1 = todo.getTime1();
        String time2 = todo.getTime2();
        String time = time1 + " " + time2;

        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer id = wxUser.getId();

        todo.setTime(DateUtil.parseTimeFormattoDate(time));
        todo.setWxNickname(wxUser.getNickName());
        todo.setWxUserId(id);
        todo.setModifyTime(new Date());

        todoService.updateById(todo);
        return ResultVo.oK();
    }


    @WxLoginToken
    @GetMapping("/get/{id}")
    @ApiOperation(value = "根据id查询详情")
    public ResultVo getById(@PathVariable Integer id) {
        return ResultVo.oK(todoService.getById(id));
    }
}
