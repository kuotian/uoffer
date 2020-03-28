/**
 * 项目名：recruitment
 * 日  期：2020/3/12
 * 包  名：com.hxxzt.recruitment.business.controller.wx
 *
 * @author： niko_hxx
 */
package com.hxxzt.recruitment.business.controller.wx;

import com.alibaba.fastjson.JSONObject;
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
import java.util.List;

@RestController
@RequestMapping("/wxApi/index")
@Slf4j
@Api(tags = "wx 微信首页相关接口")
public class WxIndexController {


    @Autowired
    private ICareerTalkService careerTalkService;

    @Autowired
    private IRecruitmentInfoService recruitmentInfoService;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IResumeService resumeService;

    @Autowired
    private IResumeSendService resumeSendService;

    @Autowired
    private IFocusService focusService;

    @Autowired
    private IClickService clickService;

    @Autowired
    private ICommentService commentService;

    @GetMapping
    @ApiOperation(value = "首页接口")
    public ResultVo index() {

        LambdaQueryWrapper<CareerTalk> careerTalkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        careerTalkLambdaQueryWrapper.orderByDesc(CareerTalk::getCreateTime);

        LambdaQueryWrapper<RecruitmentInfo> recruitmentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recruitmentInfoLambdaQueryWrapper.orderByDesc(RecruitmentInfo::getCreateTime);

        List<CareerTalk> careerTalkList = careerTalkService.list(careerTalkLambdaQueryWrapper);
        List<RecruitmentInfo> recruitmentInfoList = recruitmentInfoService.list(recruitmentInfoLambdaQueryWrapper);
        JSONObject data = new JSONObject();
        data.put("careerTalkList", careerTalkList);
        data.put("recruitmentInfoList", recruitmentInfoList);
        return ResultVo.oK(data);
    }

    @GetMapping("/recruitment/{id}")
    @ApiOperation(value = "首页查看招聘信息详情接口,并返回留言详情、点赞数、关注数")
    @WxLoginToken
    public ResultVo getRecruitmentById(@PathVariable Integer id, HttpServletRequest httpServletRequest) {

        Integer type = 1;
        JSONObject data = new JSONObject();
        String wxOpenId = JWTUtil.getWxOpenId(httpServletRequest);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        if (wxUser.getIsStudent()) {
            List<Comment> commentList = commentService.selectCommonByStudent(wxUser.getId(), type);

            Integer focusCount = focusService.selectFocusCountByStudent(wxUser.getId(), type);

            Integer clickCount = clickService.selectClickCountByStudent(wxUser.getId(), type);
            RecruitmentInfo details = recruitmentInfoService.getById(id);
            data.put("details", details);
            data.put("focusCount", focusCount);
            data.put("clickCount", clickCount);
            data.put("commentList", commentList);
            return ResultVo.oK(data);
        } else {
            List<Comment> commentList = commentService.selectCommonByNotStudent(type);

            Integer focusCount = focusService.selectFocusCountByNotStudent(type);

            Integer clickCount = clickService.selectClickCountByNotStudent(type);
            RecruitmentInfo details = recruitmentInfoService.getById(id);
            data.put("details", details);
            data.put("focusCount", focusCount);
            data.put("clickCount", clickCount);
            data.put("commentList", commentList);
            return ResultVo.oK(data);
        }

//        JSONObject data = new JSONObject();
//        LambdaQueryWrapper<Focus> focusLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        focusLambdaQueryWrapper.eq(Focus::getType, 1);
//        focusLambdaQueryWrapper.eq(Focus::getTypeId, id);
//        int focusCount = focusService.count(focusLambdaQueryWrapper);
//
//        LambdaQueryWrapper<Click> clickLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        clickLambdaQueryWrapper.eq(Click::getType, 1);
//        clickLambdaQueryWrapper.eq(Click::getTypeId, id);
//        int clickCount = clickService.count(clickLambdaQueryWrapper);
//
//        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        commentLambdaQueryWrapper.eq(Comment::getType, 1);
//        commentLambdaQueryWrapper.eq(Comment::getTypeId, id);
//        commentLambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
//        List<Comment> commentList = commentService.list(commentLambdaQueryWrapper);
//
//        RecruitmentInfo details = recruitmentInfoService.getById(id);
//        data.put("details", details);
//        data.put("focusCount", focusCount);
//        data.put("clickCount", clickCount);
//        data.put("commentList", commentList);
//        return ResultVo.oK(data);
    }

    @GetMapping("/careerTalk/{id}")
    @ApiOperation(value = "首页查看宣讲会详情接口,并返回留言详情、点赞数、关注数")
    @WxLoginToken
    public ResultVo getCareerTalkById(@PathVariable Integer id, HttpServletRequest httpServletRequest) {

        Integer type = 2;
        JSONObject data = new JSONObject();
        String wxOpenId = JWTUtil.getWxOpenId(httpServletRequest);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        if (wxUser.getIsStudent()) {
            List<Comment> commentList = commentService.selectCommonByStudent(wxUser.getId(), type);

            Integer focusCount = focusService.selectFocusCountByStudent(wxUser.getId(), type);

            Integer clickCount = clickService.selectClickCountByStudent(wxUser.getId(), type);
            CareerTalk details = careerTalkService.getById(id);
            data.put("details", details);
            data.put("focusCount", focusCount);
            data.put("clickCount", clickCount);
            data.put("commentList", commentList);
            return ResultVo.oK(data);
        } else {
            List<Comment> commentList = commentService.selectCommonByNotStudent(type);

            Integer focusCount = focusService.selectFocusCountByNotStudent(type);

            Integer clickCount = clickService.selectClickCountByNotStudent(type);
            CareerTalk details = careerTalkService.getById(id);
            data.put("details", details);
            data.put("focusCount", focusCount);
            data.put("clickCount", clickCount);
            data.put("commentList", commentList);
            return ResultVo.oK(data);
        }


//        LambdaQueryWrapper<Focus> focusLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        focusLambdaQueryWrapper.eq(Focus::getType, 2);
//        focusLambdaQueryWrapper.eq(Focus::getTypeId, id);
//        int focusCount = focusService.count(focusLambdaQueryWrapper);
//
//        LambdaQueryWrapper<Click> clickLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        clickLambdaQueryWrapper.eq(Click::getType, 2);
//        clickLambdaQueryWrapper.eq(Click::getTypeId, id);
//        int clickCount = clickService.count(clickLambdaQueryWrapper);
//
//        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        commentLambdaQueryWrapper.eq(Comment::getType, 2);
//        commentLambdaQueryWrapper.eq(Comment::getTypeId, id);
//        commentLambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
//        List<Comment> commentList = commentService.list(commentLambdaQueryWrapper);
//
//        CareerTalk details = careerTalkService.getById(id);
//        data.put("details", details);
//        data.put("focusCount", focusCount);
//        data.put("clickCount", clickCount);
//        data.put("commentList", commentList);
//        return ResultVo.oK(data);
    }

    /**
     * 投递
     *
     * @param id 简历Id
     * @return
     */
    @PutMapping("/sendResume/{id}")
    @ApiOperation(value = "招聘信息投递简历")
    @WxLoginToken
    public ResultVo sendResume(@PathVariable Integer id, HttpServletRequest request) {
        // 通过token 拿到openId
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();
        // 查询是否已经投递
        ResumeSend send = resumeSendService.isSend(wxUserId, id);
        if (send != null) {
            return ResultVo.failed(201, "已经投递过，禁止重复投递");
        }

        // 查询是否有简历
        List<Resume> resumeList = resumeService.getByWxUserId(wxUserId);
        // 只有一个简历
        if (resumeList.size() > 0 && resumeList.size() == 1) {
            Resume resume = resumeList.get(0);
            ResumeSend resumeSend = new ResumeSend();
            resumeSend.setInfoId(id);
            resumeSend.setResumeId(resume.getId());
            resumeSendService.save(resumeSend);
            return ResultVo.oK();
        } else if (resumeList.size() > 1) {
            for (Resume re : resumeList) {
                // 查询默认简历
                if (re.getIsDefault()) {
                    ResumeSend resumeSend = new ResumeSend();
                    resumeSend.setInfoId(id);
                    resumeSend.setResumeId(re.getId());
                    resumeSendService.save(resumeSend);
                }
            }
            return ResultVo.oK();
        } else {
            return ResultVo.failed(201, "无简历，请新建简历后重试！");
        }
    }

    /**
     * 职位搜索
     */
    @GetMapping("/search")
    @ApiOperation(value = "职位搜索")
    public ResultVo search(@RequestParam String position) {
        LambdaQueryWrapper<RecruitmentInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(RecruitmentInfo::getPosition, position).orderByDesc(RecruitmentInfo::getCreateTime);
        return ResultVo.oK(recruitmentInfoService.list(lambdaQueryWrapper));
    }
}
