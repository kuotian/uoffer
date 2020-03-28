package com.hxxzt.recruitment.business.controller.admin;


import com.hxxzt.recruitment.business.entity.Resume;
import com.hxxzt.recruitment.business.service.IResumeService;
import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.common.manager.UserManager;
import com.hxxzt.recruitment.common.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 简历表 前端控制器
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-05
 */
@RestController
@RequestMapping("/adminApi/resume")
@Slf4j
@Api(tags = "简历相关接口")
public class AdminResumeController {


    @Autowired
    private UserManager userManager;

    @Autowired
    private IResumeService resumeService;

    private String message;

    @GetMapping
    @ApiOperation(value = "根据条件分页查询")
    @RequiresPermissions("bus:resume:view")
    public ResultVo getPage(QueryRequest request, Resume resume) {
        return ResultVo.oK(resumeService.queryPageByPojo(request, resume));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询单个")
    @RequiresPermissions("bus:resume:query")
    public ResultVo getOne(@PathVariable("id") Integer id) {
        return ResultVo.oK(resumeService.getById(id));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据ID删除数据")
    @RequiresPermissions("bus:resume:del")
    @Log(module = "微信管理", operation = "根据ID删除简历数据")
    public ResultVo remove(@PathVariable("id") Integer[] id) throws Exception {
        try {
            if (resumeService.removeByIds(Arrays.asList(id))) {
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
    @RequiresPermissions("bus:resume:edit")
    @Log(module = "微信管理", operation = "根据ID修改简历数据")
    public ResultVo update(@RequestBody Resume resume) throws Exception {
        try {
            if (resumeService.updateById(resume)) {
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
