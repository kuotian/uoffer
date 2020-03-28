package com.hxxzt.recruitment.business.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxxzt.recruitment.business.entity.Focus;
import com.hxxzt.recruitment.business.service.IFocusService;
import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
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
 * 关注表 前端控制器
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@RestController
@RequestMapping("/adminApi/focus")
@Slf4j
@Api(tags = "关注表相关接口")
public class AdminFocusController {

    @Autowired
    private IFocusService focusService;

    private String message;

    @GetMapping
    @ApiOperation(value = "根据条件分页查询")
    @RequiresPermissions("bus:focus:view")
    public ResultVo getPage(QueryRequest request, Focus focus) {
        return ResultVo.oK(focusService.queryPageByPojo(request, focus));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询单个")
    public ResultVo getOne(@PathVariable("id") Integer id) {
        return ResultVo.oK(focusService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public ResultVo insert(@RequestBody Focus focus) throws Exception {
        try {
            if (focusService.save(focus)) {
                return ResultVo.oK();
            } else {
                message = "新增失败";
                return ResultVo.failed(201, message);
            }
        } catch (Exception e) {
            message = "新增失败";
            log.error(message, e);
            throw new Exception(message);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据ID删除数据")
    @RequiresPermissions("bus:focus:del")
    @Log(module = "微信管理", operation = "根据ID删除关注数据")
    public ResultVo remove(@PathVariable("id") Integer[] id) throws Exception {
        try {
            if (focusService.removeByIds(Arrays.asList(id))) {
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
    public ResultVo update(@RequestBody Focus focus) throws Exception {
        try {
            if (focusService.updateById(focus)) {
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

    @ApiOperation(value = "通过宣讲会Id查看关注")
    @GetMapping("/getCareerTalkFocus/{id}")
    public ResultVo getCareerTalkFocus(@PathVariable Integer id) {

        LambdaQueryWrapper<Focus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Focus::getType, 2);// 宣讲会type为2
        lambdaQueryWrapper.eq(Focus::getTypeId, id);
        return ResultVo.oK(focusService.list(lambdaQueryWrapper));
    }

    @ApiOperation(value = "通过招聘Id查看关注")
    @GetMapping("/getRecruitmentInfoFocus/{id}")
    public ResultVo getRecruitmentInfoFocus(@PathVariable Integer id) {

        LambdaQueryWrapper<Focus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Focus::getType, 1);// 招聘type为1
        lambdaQueryWrapper.eq(Focus::getTypeId, id);
        return ResultVo.oK(focusService.list(lambdaQueryWrapper));
    }

}
