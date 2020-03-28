package com.hxxzt.recruitment.business.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxxzt.recruitment.business.entity.Click;
import com.hxxzt.recruitment.business.service.IClickService;
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
 * 点赞表 前端控制器
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@RestController
@RequestMapping("/adminApi/click")
@Slf4j
@Api(tags = "点赞表相关接口")
public class AdminClickController {

    @Autowired
    private IClickService clickService;

    private String message;

    @GetMapping
    @ApiOperation(value = "根据条件分页查询")
    @RequiresPermissions("bus:click:view")
    public ResultVo getPage(QueryRequest request, Click click) {
        return ResultVo.oK(clickService.queryPageByPojo(request, click));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询单个")
    public ResultVo getOne(@PathVariable("id") Integer id) {
        return ResultVo.oK(clickService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public ResultVo insert(@RequestBody Click click) throws Exception {
        try {
            if (clickService.save(click)) {
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
    @RequiresPermissions("bus:click:del")
    @Log(module = "微信管理", operation = "根据ID删除点赞数据")
    public ResultVo remove(@PathVariable("id") Integer[] id) throws Exception {
        try {
            if (clickService.removeByIds(Arrays.asList(id))) {
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
    public ResultVo update(@RequestBody Click click) throws Exception {
        try {
            if (clickService.updateById(click)) {
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

    @ApiOperation(value = "通过宣讲会Id查看点赞")
    @GetMapping("/getCareerTalkClick/{id}")
    public ResultVo getCareerTalkClick(@PathVariable Integer id) {

        LambdaQueryWrapper<Click> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Click::getType, 2);// 宣讲会type为2
        lambdaQueryWrapper.eq(Click::getTypeId, id);
        return ResultVo.oK(clickService.list(lambdaQueryWrapper));
    }

    @ApiOperation(value = "通过招聘Id查看点赞")
    @GetMapping("/getRecruitmentInfoClick/{id}")
    public ResultVo getRecruitmentInfoClick(@PathVariable Integer id) {

        LambdaQueryWrapper<Click> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Click::getType, 1);// 招聘type为1
        lambdaQueryWrapper.eq(Click::getTypeId, id);
        return ResultVo.oK(clickService.list(lambdaQueryWrapper));
    }
}
