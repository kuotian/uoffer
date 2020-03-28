package com.hxxzt.recruitment.business.controller.admin;


import com.hxxzt.recruitment.business.entity.CareerTalk;
import com.hxxzt.recruitment.business.service.ICareerTalkService;
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
 * 宣讲会表 前端控制器
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@RestController
@RequestMapping("/adminApi/careerTalk")
@Slf4j
@Api(tags = "宣讲会相关接口")
public class AdminCareerTalkController {


    @Autowired
    private ICareerTalkService careerTalkService;

    private String message;

    @Autowired
    private UserManager userManager;

    @GetMapping
    @ApiOperation(value = "根据条件分页查询")
    @RequiresPermissions("bus:careerTalk:view")
    public ResultVo getPage(QueryRequest request, CareerTalk careerTalk, HttpServletRequest httpServletRequest) {
        Integer userId = JWTUtil.getUserId(httpServletRequest);
        if (!userManager.isEnterpriseRole(userId)) {
            return ResultVo.oK(careerTalkService.queryPageByPojo(request, careerTalk));
        } else {
            careerTalk.setUserId(userId);
            return ResultVo.oK(careerTalkService.queryPageByPojo(request, careerTalk));
        }
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询单个")
    @RequiresPermissions("bus:careerTalk:query")
    public ResultVo getOne(@PathVariable("id") Integer id) {
        return ResultVo.oK(careerTalkService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    @Log(module = "信息发布", operation = "发布宣讲会信息")
    public ResultVo insert(@RequestBody CareerTalk careerTalk, HttpServletRequest request) throws Exception {
        Integer userId = JWTUtil.getUserId(request);
        String username = JWTUtil.getUsername(request);
        try {
            if (userManager.isAdmin(userId)) {
                careerTalk.setUserId(userId);
                careerTalk.setUsername(username);
                if (careerTalkService.save(careerTalk)) {
                    return ResultVo.oK();
                } else {
                    message = "新增失败";
                    return ResultVo.failed(201, message);
                }
            }
            // 发布前判断是否被管理员禁止发布
            if (!userManager.isPublic(userId)) {
                return ResultVo.failed(201, "管理员禁止发布，请联系管理员");
            } else {
                careerTalk.setUserId(userId);
                careerTalk.setUsername(username);
                if (careerTalkService.save(careerTalk)) {
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
    @RequiresPermissions("bus:careerTalk:del")
    @Log(module = "信息管理", operation = "根据ID删除宣讲会数据")
    public ResultVo remove(@PathVariable Integer[] id) throws Exception {
        try {
            if (careerTalkService.removeByIds(Arrays.asList(id))) {
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
    @RequiresPermissions("bus:careerTalk:edit")
    @Log(module = "信息管理", operation = "根据ID修改宣讲会数据")
    public ResultVo update(@RequestBody CareerTalk careerTalk) throws Exception {
        try {
            if (careerTalkService.updateById(careerTalk)) {
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
