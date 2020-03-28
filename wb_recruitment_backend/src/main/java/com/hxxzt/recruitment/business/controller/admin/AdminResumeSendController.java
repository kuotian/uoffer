package com.hxxzt.recruitment.business.controller.admin;


import com.hxxzt.recruitment.business.entity.RecruitmentInfo;
import com.hxxzt.recruitment.business.entity.Resume;
import com.hxxzt.recruitment.business.entity.ResumeSend;
import com.hxxzt.recruitment.business.entity.Todo;
import com.hxxzt.recruitment.business.service.IRecruitmentInfoService;
import com.hxxzt.recruitment.business.service.IResumeSendService;
import com.hxxzt.recruitment.business.service.IResumeService;
import com.hxxzt.recruitment.business.service.ITodoService;
import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.common.manager.UserManager;
import com.hxxzt.recruitment.common.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 简历投递表 前端控制器
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-05
 */
@RestController
@RequestMapping("/adminApi/resumeSend")
@Slf4j
@Api(tags = "简历投递相关接口")
public class AdminResumeSendController {


    @Autowired
    private UserManager userManager;

    @Autowired
    private IResumeSendService resumeSendService;

    @Autowired
    private IResumeService resumeService;

    @Autowired
    private ITodoService todoService;

    @Autowired
    private IRecruitmentInfoService recruitmentInfoService;

    @GetMapping
    @ApiOperation(value = "此接口企业管理员只能看到自己企业发布招聘下的简历，管理员可查看全部")
    @RequiresPermissions("bus:resumeSend:view")
    public ResultVo list(QueryRequest request, String name, String major, HttpServletRequest httpServletRequest) {
        Integer userId = JWTUtil.getUserId(httpServletRequest);
        if (userManager.isEnterpriseRole(userId)) { // 如果为企业用户
            return ResultVo.oK(resumeSendService.selectByEnterprise(request, name, major, userId));
        } else {
            return ResultVo.oK(resumeSendService.select(request, name, major));
        }
    }

    @GetMapping("/{resumeSendId}")
    @ApiOperation(value = "查看用户投递简历")
    @RequiresPermissions("bus:resumeSend:query")
    public ResultVo getInfo(@PathVariable Integer resumeSendId, HttpServletRequest httpServletRequest) {
        Integer userId = JWTUtil.getUserId(httpServletRequest);
        if (userManager.isEnterpriseRole(userId)) { // 如果为企业用户,查看需标记已查看
            resumeSendService.tagCheck(resumeSendId);
            return ResultVo.oK(resumeService.getResumeByResumeSendId(resumeSendId));
        } else {
            return ResultVo.oK(resumeService.getResumeByResumeSendId(resumeSendId));
        }
    }

    @PutMapping("/invite")
    @ApiOperation(value = "发送邀请")
    @Log(module = "信息管理", operation = "发送面试邀请")
    public ResultVo invite(@RequestBody ResumeSend resumeSend) {
        ResumeSend rs = resumeSendService.getById(resumeSend.getId());
        if (rs.getStatus() != null && rs.getStatus().equals(1)) {
            return ResultVo.failed(201, "已标记不符合要求，禁止发送面试邀请");
        }
        if (rs.getStatus() != null && rs.getStatus().equals(2)) {
            return ResultVo.failed(201, "已发送面试邀请，禁止再次点击");
        }

        resumeSend.setInterviewStatus(0);
        resumeSendService.updateById(resumeSend);

        // 面试邀请存入todo
        RecruitmentInfo recruitmentInfo = recruitmentInfoService.getById(rs.getInfoId());
        Resume resume = resumeService.getById(rs.getResumeId());
        Todo todo = new Todo();
        todo.setContent("面试：" + recruitmentInfo.getEntName());
        todo.setAddress(recruitmentInfo.getEntAddress());
        todo.setTime(resumeSend.getInterviewTime());
        todo.setWxUserId(resume.getWxUserId());
        todo.setWxNickname(resume.getWxNickname());
        todoService.save(todo);

        return ResultVo.oK();
    }

    @PutMapping("/inadequacy")
    @ApiOperation(value = "标记简历不符合要求")
    @Log(module = "信息管理", operation = "标记简历不符合要求")
    public ResultVo inadequacy(@RequestBody ResumeSend resumeSend) {
        ResumeSend rs = resumeSendService.getById(resumeSend.getId());
        if (rs.getStatus() != null && rs.getStatus().equals(2)) {
            return ResultVo.failed(201, "已发送面试邀请，禁止设置不符合要求");
        }
        if (rs.getStatus() != null && rs.getStatus().equals(1)) {
            return ResultVo.failed(201, "已标记不符合要求，禁止再次点击");
        }

        resumeSendService.updateById(resumeSend);
        return ResultVo.oK();
    }

}
