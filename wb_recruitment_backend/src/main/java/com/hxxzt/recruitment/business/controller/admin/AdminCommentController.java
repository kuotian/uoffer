package com.hxxzt.recruitment.business.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxxzt.recruitment.business.entity.Comment;
import com.hxxzt.recruitment.business.service.ICommentService;
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
 * 评论表 前端控制器
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@RestController
@RequestMapping("/adminApi/comment")
@Slf4j
@Api(tags = "评论表相关接口")
public class AdminCommentController {

    @Autowired
    private ICommentService commentService;

    private String message;

    @GetMapping
    @ApiOperation(value = "根据条件分页查询")
    @RequiresPermissions("bus:comment:view")
    public ResultVo getPage(QueryRequest request, Comment comment) {
        return ResultVo.oK(commentService.queryPageByPojo(request, comment));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询单个")
    @RequiresPermissions("bus:comment:query")
    public ResultVo getOne(@PathVariable("id") Integer id) {
        return ResultVo.oK(commentService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public ResultVo insert(@RequestBody Comment comment) throws Exception {
        try {
            if (commentService.save(comment)) {
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
    @RequiresPermissions("bus:comment:del")
    @Log(module = "微信管理", operation = "根据ID删除评论数据")
    public ResultVo remove(@PathVariable("id") Integer[] id) throws Exception {
        try {
            if (commentService.removeByIds(Arrays.asList(id))) {
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
    @RequiresPermissions("bus:comment:edit")
    @Log(module = "微信管理", operation = "根据ID修改评论数据")
    public ResultVo update(@RequestBody Comment comment) throws Exception {
        try {
            if (commentService.updateById(comment)) {
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

    @ApiOperation(value = "通过宣讲会Id查看评论")
    @GetMapping("/getCareerTalkComment/{id}")
    public ResultVo getCareerTalkComment(@PathVariable Integer id) {

        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getType, 2);// 宣讲会type为2
        lambdaQueryWrapper.eq(Comment::getTypeId, id);
        return ResultVo.oK(commentService.list(lambdaQueryWrapper));
    }

    @ApiOperation(value = "通过招聘Id查看评论")
    @GetMapping("/getRecruitmentInfoComment/{id}")
    public ResultVo getRecruitmentInfoComment(@PathVariable Integer id) {

        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getType, 1);// 招聘type为1
        lambdaQueryWrapper.eq(Comment::getTypeId, id);
        return ResultVo.oK(commentService.list(lambdaQueryWrapper));
    }
}
