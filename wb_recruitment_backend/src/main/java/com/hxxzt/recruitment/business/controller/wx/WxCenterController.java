/**
 * 项目名：recruitment
 * 日  期：2020/3/13
 * 包  名：com.hxxzt.recruitment.business.controller.wx
 *
 * @author： niko_hxx
 */
package com.hxxzt.recruitment.business.controller.wx;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxxzt.recruitment.business.entity.Resume;
import com.hxxzt.recruitment.business.entity.ResumeSend;
import com.hxxzt.recruitment.business.entity.WxUser;
import com.hxxzt.recruitment.business.service.IRecruitmentInfoService;
import com.hxxzt.recruitment.business.service.IResumeSendService;
import com.hxxzt.recruitment.business.service.IResumeService;
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
 * 微信我的个人中心相关接口
 */
@RestController
@RequestMapping("/wxApi/center")
@Slf4j
@Api(tags = "wx 微信我的个人中心相关接口")
public class WxCenterController {

    @Autowired
    private IResumeService resumeService;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IResumeSendService resumeSendService;

    @Autowired
    private IRecruitmentInfoService recruitmentInfoService;

    /**
     * 查询我的简历列表
     */
    @GetMapping("/resume")
    @WxLoginToken
    @ApiOperation(value = "查询我的简历列表")
    public ResultVo getMyResumeList(HttpServletRequest request) {
        // 通过token 拿到openId
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();
        LambdaQueryWrapper<Resume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Resume::getWxUserId, wxUserId);
        lambdaQueryWrapper.orderByDesc(Resume::getIsDefault, Resume::getCreateTime);
        return ResultVo.oK(resumeService.list(lambdaQueryWrapper));
    }

    /**
     * 创建简历，创建第一个简历需要设置为默认简历。
     */
    @PostMapping("/resume")
    @WxLoginToken
    @ApiOperation(value = "创建简历")
    public ResultVo createResume(@RequestBody Resume resume, HttpServletRequest request) {
        // 通过token 拿到openId
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();
        List<Resume> resumeList = resumeService.getByWxUserId(wxUserId);
        if (resumeList.size() == 0) {
            resume.setIsDefault(true);
        } else {
            resume.setIsDefault(false);
        }
        resume.setWxUserId(wxUserId);
        resume.setWxNickname(wxUser.getNickName());
        resumeService.save(resume);

        return ResultVo.oK();
    }

    /**
     * 修改简历
     */
    @PutMapping("/resume")
    @WxLoginToken
    @ApiOperation(value = "修改简历")
    public ResultVo modifyResume(@RequestBody Resume resume) {
        resume.setModifyTime(new Date());
        resumeService.updateById(resume);
        return ResultVo.oK();
    }

    /**
     * 设置为默认简历，如果已是默认提示，设置为默认需要取消其他为默认的简历
     */
    @PutMapping("/resume/idDefault/{id}")
    @WxLoginToken
    @ApiOperation(value = "设置为默认简历")
    public ResultVo isDefault(@PathVariable Integer id, HttpServletRequest request) {
        // 通过token 拿到openId
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();
        LambdaQueryWrapper<Resume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Resume::getId, id);
        lambdaQueryWrapper.eq(Resume::getWxUserId, wxUserId);
        Resume resume = resumeService.getOne(lambdaQueryWrapper);
        if (resume.getIsDefault()) {
            return ResultVo.failed(201, "已经设置过默认简历");
        } else {
            // 取消其他默认简历
            List<Resume> allList = resumeService.getByWxUserId(wxUserId);
            if (allList.size() > 0) {
                for (Resume re : allList) {
                    if (re.getIsDefault()) {
                        re.setIsDefault(false);
                        resumeService.updateById(re);
                    }
                }
            }
            resume.setIsDefault(true);
            resumeService.updateById(resume);
            return ResultVo.oK();
        }
    }

    /**
     * 设置简历是否公开或者是否隐藏
     */
    @WxLoginToken
    @ApiOperation(value = "设置简历是否公开或者是否隐藏")
    @PutMapping("/resume/isHide")
    public ResultVo isHide(@RequestBody Resume resume) {
        resumeService.updateById(resume);
        return ResultVo.oK();
    }


    /**
     * 删除简历，判断是否默认，默认则判断是否有其他简历，
     * 有其他简历默认按照时间排序给默认简历
     */
    @DeleteMapping("/resume/{id}")
    @WxLoginToken
    @ApiOperation(value = "删除简历")
    public ResultVo delResume(@PathVariable Integer id, HttpServletRequest request) {
        // 通过token 拿到openId
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();

        Resume resume = resumeService.getById(id);
        // 删除后查询是否有其他简历
        resumeService.removeById(id);
        if (resume.getIsDefault()) {
            List<Resume> resumeList = resumeService.getByWxUserId(wxUserId);

            if (resumeList.size() > 0) {
                Resume re = resumeList.get(0);
                re.setIsDefault(true);
                resumeService.updateById(re);
            }
        }

        return ResultVo.oK();
    }

    /**
     * 通过Id查询简历
     */
    @GetMapping("/resume/{id}")
    @WxLoginToken
    @ApiOperation(value = "通过Id查询简历")
    public ResultVo getById(@PathVariable Integer id) {
        return ResultVo.oK(resumeService.getById(id));
    }

    /**
     * 刷新简历,一天只能请求一次
     */
    @PutMapping("/resume/refresh/{id}")
    @ApiOperation("刷新简历")
    @WxLoginToken
    public ResultVo refreshById(@PathVariable Integer id) {
        // 查询该简历今天是否刷新过
        LambdaQueryWrapper<Resume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String now = DateUtil.parseTimeFormattoDayDate(new Date());
        lambdaQueryWrapper.eq(Resume::getId, id).like(Resume::getRefreshTime, now);
        Resume resume = resumeService.getOne(lambdaQueryWrapper);
        if (resume != null) {
            return ResultVo.failed(201, "今日已刷新");
        } else {
            Resume re = new Resume();
            re.setId(id);
            re.setRefreshTime(new Date());
            resumeService.updateById(re);
            return ResultVo.oK();
        }
    }

    /**
     * 查询投递进度，全部为1，已查看2，邀请面试3，不合适4
     */
    @GetMapping("/schedule/{statusId}")
    @ApiOperation(value = "查询投递进度，全部为1，已查看2，邀请面试3，不合适4")
    @WxLoginToken
    public ResultVo scheduleList(@PathVariable Integer statusId, HttpServletRequest request) {
        // 通过token 拿到openId
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser wxUser = wxUserService.selectByOpenId(wxOpenId);
        Integer wxUserId = wxUser.getId();
        Integer status = null;
        Boolean isCheck = null;
        switch (statusId) {
            case 1:
                return ResultVo.oK(resumeSendService.queryList(wxUserId));
            case 2:
                isCheck = true;
                return ResultVo.oK(resumeSendService.queryListByIsCheck(wxUserId, isCheck));
            case 3:
                status = 2;
                return ResultVo.oK(resumeSendService.queryListByStatus(wxUserId, status));
            case 4:
                status = 1;
                return ResultVo.oK(resumeSendService.queryListByStatus(wxUserId, status));
            default:
                return ResultVo.oK(resumeSendService.queryList(wxUserId));
        }
    }


    /**
     * 接受面试邀请
     */
    @PutMapping("/accept/{resumeId}")
    @ApiOperation(value = "接受面试邀请")
    @WxLoginToken
    public ResultVo accept(@PathVariable Integer resumeId) {
        ResumeSend resumeSend = new ResumeSend();
        resumeSend.setId(resumeId);
        resumeSend.setInterviewStatus(1);
        resumeSendService.updateById(resumeSend);
        return ResultVo.oK();
    }


    /**
     * 用户资料填写
     */
    @PutMapping("/dataEntry")
    @ApiOperation(value = "用户资料录入")
    @WxLoginToken
    public ResultVo dataEntry(@RequestBody WxUser wxUser, HttpServletRequest request) {
        String wxOpenId = JWTUtil.getWxOpenId(request);
        WxUser user = wxUserService.selectByOpenId(wxOpenId);
        wxUser.setId(user.getId());
        wxUserService.updateById(wxUser);
        return ResultVo.oK();
    }

    /**
     * 邀请详情，招聘信息Id（infoId）查询招聘信息
     */
    @GetMapping("/getInfo/{rsId}")
    @WxLoginToken
    @ApiOperation(value = "邀请详情，招聘信息Id（infoId）查询招聘信息")
    public ResultVo getInfo(@PathVariable Integer rsId) {

        return ResultVo.oK(recruitmentInfoService.queryListByRsId(rsId));
    }

    /**
     * 获取用户基本信息
     */
    @GetMapping("/getWxUser")
    @WxLoginToken
    @ApiOperation(value = "获取用户基本信息")
    public ResultVo getWxUser(HttpServletRequest request) {

        String wxOpenId = JWTUtil.getWxOpenId(request);
        return ResultVo.oK(wxUserService.selectByOpenId(wxOpenId));
    }
}
