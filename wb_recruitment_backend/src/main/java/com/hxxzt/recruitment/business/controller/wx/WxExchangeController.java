/**
 * 项目名：recruitment
 * 日  期：2020/3/13
 * 包  名：com.hxxzt.recruitment.business.controller.wx
 *
 * @author： HuangXiuxiang
 */
package com.hxxzt.recruitment.business.controller.wx;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxxzt.recruitment.business.entity.*;
import com.hxxzt.recruitment.business.service.*;
import com.hxxzt.recruitment.common.aop.annotation.WxLoginToken;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/wxApi/exchange")
@RestController
@Api(tags = "评论、留言、点赞相关接口")
@Slf4j
public class WxExchangeController {


    @Autowired
    private ICommentService commentService;

    @Autowired
    private IClickService clickService;

    @Autowired
    private IFocusService focusService;


    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private ITodoService todoService;

    @Autowired
    private ICareerTalkService careerTalkService;

    /**
     * 点赞和关注先判断是否已经点过按钮，点过则禁止
     */

    @PutMapping("/comment")
    @WxLoginToken
    @ApiOperation(value = "留言,招聘信息type为1,宣讲会type为2")
    public ResultVo comment(HttpServletRequest request, @RequestBody Comment comment) {
        // 通过token 拿到openId
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        comment.setWxUserId(wxUser.getId());
        comment.setWxNickname(wxUser.getNickName());
        commentService.save(comment);
        return ResultVo.oK();
    }

    @PutMapping("/click")
    @WxLoginToken
    @ApiOperation(value = "点赞,招聘信息type为1,宣讲会type为2")
    public ResultVo click(HttpServletRequest request, @RequestBody Click click) {
        // 通过token 拿到openId
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();
        LambdaQueryWrapper<Click> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Click::getType, click.getType()).eq(Click::getTypeId, click.getTypeId()).eq(Click::getWxUserId, wxUserId);
        Click entity = clickService.getOne(lambdaQueryWrapper);
        if (entity != null) {
            return ResultVo.failed(201, "不允许重复点击");
        } else {
            click.setWxUserId(wxUserId);
            click.setWxNickname(wxUser.getNickName());
            clickService.save(click);
            return ResultVo.oK();
        }

    }

    @PutMapping("/focus")
    @WxLoginToken
    @ApiOperation(value = "关注,招聘信息type为1,宣讲会type为2")
    public ResultVo focus(HttpServletRequest request, @RequestBody Focus focus) {
        // 通过token 拿到openId
        String wxOpenId = JWTUtil.getWxOpenId(request);

        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();
        LambdaQueryWrapper<Focus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Focus::getType, focus.getType()).eq(Focus::getTypeId, focus.getTypeId()).eq(Focus::getWxUserId, wxUserId);
        Focus entity = focusService.getOne(lambdaQueryWrapper);
        if (entity != null) {
            return ResultVo.failed(201, "不允许重复点击");
        } else {
            focus.setWxUserId(wxUserId);
            focus.setWxNickname(wxUser.getNickName());
            focusService.save(focus);

            // 宣讲会存入todoList
            if (focus.getType() == 2) {
                CareerTalk careerTalk = careerTalkService.getById(focus.getTypeId());
                Todo todo = new Todo();
                todo.setContent(careerTalk.getContent());
                todo.setAddress(careerTalk.getAddress());
                todo.setTime(careerTalk.getTime());
                todo.setWxUserId(wxUserId);
                todo.setWxNickname(wxUser.getNickName());
                todoService.save(todo);
            }

            return ResultVo.oK();
        }
    }


    @GetMapping("/common")
    @WxLoginToken
    @ApiOperation(value = "查询评论列表")
    public ResultVo commentList(HttpServletRequest request) {
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();
        return ResultVo.oK(commentService.selectListByWxUserId(wxUserId));
    }

    @GetMapping("/click")
    @WxLoginToken
    @ApiOperation(value = "查询点赞列表")
    public ResultVo clickList(HttpServletRequest request) {
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();
        return ResultVo.oK(clickService.selectListByWxUserId(wxUserId));
    }

    @GetMapping("/focus")
    @WxLoginToken
    @ApiOperation(value = "查询关注列表")
    public ResultVo focusList(HttpServletRequest request) {
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();
        return ResultVo.oK(focusService.selectListByWxUserId(wxUserId));
    }


}
