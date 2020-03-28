package com.hxxzt.recruitment.business.controller.admin;


import com.hxxzt.recruitment.business.entity.WxUser;
import com.hxxzt.recruitment.business.service.IWxUserService;
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
 * 微信小程序用户 前端控制器
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-02
 */
@RestController
@RequestMapping("/adminApi/wxUser")
@Slf4j
@Api(tags = "微信小程序用户相关接口")
public class AdminWxUserController {

    @Autowired
    private IWxUserService wxUserService;

    private String message;

    @GetMapping
    @ApiOperation(value = "根据条件分页查询")
    @RequiresPermissions("bus:wxUser:view")
    public ResultVo getPage(QueryRequest request, WxUser wxUser) {
        return ResultVo.oK(wxUserService.queryPageByPojo(request, wxUser));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询单个")
    @RequiresPermissions("bus:wxUser:query")
    public ResultVo getOne(@PathVariable("id") Integer id) {
        return ResultVo.oK(wxUserService.getById(id));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据ID删除数据")
    @RequiresPermissions("bus:wxUser:del")
    @Log(module = "微信管理", operation = "根据ID删除微信用户数据")
    public ResultVo remove(@PathVariable("id") Integer[] id) throws Exception {
        try {
            if (wxUserService.removeByIds(Arrays.asList(id))) {
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
    public ResultVo update(@RequestBody WxUser wxUser) throws Exception {
        try {
            if (wxUserService.updateById(wxUser)) {
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

    @PutMapping("/changeIsSpeak")
    @ApiOperation(value = "禁止/不禁止发表评论")
    @Log(module = "微信管理", operation = "禁止/不禁止微信用户发表评论")
    public ResultVo changeIsSpeak(@RequestBody WxUser wxUser) throws Exception {
        try {
            if (wxUserService.updateById(wxUser)) {
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
