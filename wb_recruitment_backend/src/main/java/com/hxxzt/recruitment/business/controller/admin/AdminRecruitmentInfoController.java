package com.hxxzt.recruitment.business.controller.admin;


import com.hxxzt.recruitment.business.entity.RecruitmentInfo;
import com.hxxzt.recruitment.business.service.IRecruitmentInfoService;
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
import java.util.Arrays;

/**
 * <p>
 * 招聘信息 前端控制器
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@RestController
@RequestMapping("/adminApi/recruitmentInfo")
@Slf4j
@Api(tags = "招聘信息相关接口")
public class AdminRecruitmentInfoController {

    @Autowired
    private IRecruitmentInfoService recruitmentInfoService;

    private String message;

    @Autowired
    private UserManager userManager;

    @GetMapping
    @ApiOperation(value = "根据条件分页查询")
    @RequiresPermissions("bus:recruitment:view")
    public ResultVo getPage(QueryRequest request, RecruitmentInfo recruitmentInfo, HttpServletRequest httpServletRequest) {

        Integer userId = JWTUtil.getUserId(httpServletRequest);
        if (!userManager.isEnterpriseRole(userId)) {
            return ResultVo.oK(recruitmentInfoService.queryPageByPojo(request, recruitmentInfo));
        } else {
            recruitmentInfo.setUserId(userId);
            return ResultVo.oK(recruitmentInfoService.queryPageByPojo(request, recruitmentInfo));
        }
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询单个")
    @RequiresPermissions("bus:recruitment:query")
    public ResultVo getOne(@PathVariable("id") Integer id) {
        return ResultVo.oK(recruitmentInfoService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    @Log(module = "信息发布", operation = "发布招聘信息")
    public ResultVo insert(@RequestBody RecruitmentInfo recruitmentInfo, HttpServletRequest request) throws Exception {
        Integer userId = JWTUtil.getUserId(request);
        String username = JWTUtil.getUsername(request);
        try {
            if (userManager.isAdmin(userId)) {
                if (recruitmentInfo.getIsNegotiable()) { // 如果面议 薪资范围设为null
                    recruitmentInfo.setSalary(null);
                } else {
                    String salaryUp = recruitmentInfo.getSalaryUp();
                    String salaryDown = recruitmentInfo.getSalaryDown();
                    recruitmentInfo.setSalary(salaryUp + "-" + salaryDown);
                }
                recruitmentInfo.setUserId(userId);
                recruitmentInfo.setUsername(username);
                if (recruitmentInfoService.save(recruitmentInfo)) {
                    return ResultVo.oK();
                } else {
                    message = "新增失败";
                    return ResultVo.failed(201, message);
                }
            }
            if (!userManager.isPublic(userId)) {
                return ResultVo.failed(201, "管理员禁止发布，请联系管理员");
            } else {
                if (recruitmentInfo.getIsNegotiable()) { // 如果面议 薪资范围设为null
                    recruitmentInfo.setSalary(null);
                } else {
                    String salaryUp = recruitmentInfo.getSalaryUp();
                    String salaryDown = recruitmentInfo.getSalaryDown();
                    recruitmentInfo.setSalary(salaryUp + "-" + salaryDown);
                }
                recruitmentInfo.setUserId(userId);
                recruitmentInfo.setUsername(username);
                if (recruitmentInfoService.save(recruitmentInfo)) {
                    return ResultVo.oK();
                } else {
                    message = "新增失败";
                    return ResultVo.failed(201, message);
                }
            }
        } catch (Exception e) {
            message = "新增失败";
            log.error(message, e);
            throw new Exception(message);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据ID删除数据")
    @RequiresPermissions("bus:recruitment:del")
    @Log(module = "信息管理", operation = "根据ID删除招聘数据")
    public ResultVo remove(@PathVariable("id") Integer[] id) throws Exception {
        try {
            if (recruitmentInfoService.removeByIds(Arrays.asList(id))) {
                return ResultVo.oK();
            } else {
                message = "删除失败";
                return ResultVo.failed(201, message);
            }
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new Exception(message);
        }
    }

    @PutMapping
    @ApiOperation(value = "根据ID修改数据")
    @RequiresPermissions("bus:recruitment:edit")
    @Log(module = "信息管理", operation = "根据ID修改招聘数据")
    public ResultVo update(@RequestBody RecruitmentInfo recruitmentInfo) throws Exception {
        try {
            if (recruitmentInfo.getIsNegotiable()) {
                recruitmentInfo.setSalary(null);
            }
            if (recruitmentInfoService.updateById(recruitmentInfo)) {
                return ResultVo.oK();
            } else {
                message = "修改失败";
                return ResultVo.failed(201, message);
            }
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new Exception(message);
        }
    }
}
