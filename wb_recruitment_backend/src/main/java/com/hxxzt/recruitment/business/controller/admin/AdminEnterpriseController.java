package com.hxxzt.recruitment.business.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hxxzt.recruitment.business.entity.Enterprise;
import com.hxxzt.recruitment.business.service.IEnterpriseService;
import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.common.exception.PenintException;
import com.hxxzt.recruitment.common.properties.PenintProperties;
import com.hxxzt.recruitment.common.utils.SpringContextUtil;
import com.hxxzt.recruitment.common.utils.UploadFileUtils;
import com.hxxzt.recruitment.common.vo.ResultVo;
import com.hxxzt.recruitment.system.service.ISysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 企业信息表 前端控制器
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@RestController
@RequestMapping("/adminApi/enterprise")
@Slf4j
@Api(tags = "企业信息表相关接口")
public class AdminEnterpriseController {

    @Autowired
    private IEnterpriseService enterpriseService;

    @Autowired
    private ISysUserRoleService userRoleService;

    private String message;

    @GetMapping("/notReview")
    @ApiOperation(value = "根据条件分页查询待审核数据")
    @RequiresPermissions("bus:notReviewEnt:view")
    public ResultVo notReview(QueryRequest request, Enterprise enterprise) {
        enterprise.setReviewStatus(false);
        return ResultVo.oK(enterpriseService.queryPageByPojo(request, enterprise));
    }

    @GetMapping("/review")
    @ApiOperation(value = "根据条件分页查询已审核数据")
    @RequiresPermissions("bus:reviewEnt:view")
    public ResultVo review(QueryRequest request, Enterprise enterprise) {
        enterprise.setReviewStatus(true);
        return ResultVo.oK(enterpriseService.queryPageByPojo(request, enterprise));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询单个")
    @RequiresPermissions(value = {"bus:reviewEnt:query", "bus:notReviewEnt:query"}, logical = Logical.OR)
    public ResultVo getOne(@PathVariable("id") Integer id) {
        return ResultVo.oK(enterpriseService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    @Log(module = "企业认证", operation = "新注册用户企业认证")
    public ResultVo insert(@RequestBody Enterprise enterprise, HttpServletRequest request) throws Exception {
        Integer userId = JWTUtil.getUserId(request);
        String username = JWTUtil.getUsername(request);
        enterprise.setUserId(userId);
        enterprise.setUsername(username);
        try {
            if (enterpriseService.queryByUserId(userId) != null) {
                return ResultVo.failed(201, "已新增过企业,待审核完成！");
            } else {
                if (enterpriseService.save(enterprise)) {
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
    @RequiresPermissions("bus:reviewEnt:del")
    @Log(module = "企业管理", operation = "根据Id删除企业")
    public ResultVo remove(@PathVariable("id") Integer id) throws Exception {
        try {
            if (enterpriseService.removeById(id)) {
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
    public ResultVo update(@RequestBody Enterprise enterprise) throws Exception {
        try {
            if (enterpriseService.updateById(enterprise)) {
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

    @PutMapping("/review/{id}")
    @ApiOperation(value = "审核企业")
    @RequiresPermissions("bus:notReviewEnt:review")
    @Log(module = "企业管理", operation = "审核企业")
    public ResultVo review(@PathVariable Integer id, HttpServletRequest request) throws Exception {
        String username = JWTUtil.getUsername(request);
        Enterprise enterprise = new Enterprise();
        enterprise.setReviewPeople(username);
        enterprise.setReviewStatus(true);
        enterprise.setIsPublish(true);
        enterprise.setReviewTime(new Date());
        enterprise.setId(id);
        try {
            if (enterpriseService.updateById(enterprise)) {
                // 审核完毕，给予该用户为企业用户角色，企业用户角色id为72
                userRoleService.updateRoleIdByUserId(72, enterpriseService.getById(id).getUserId());
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

    @PutMapping("/changePublish")
    @ApiOperation(value = "禁止/不禁止发布")
    @Log(module = "企业管理", operation = "禁止/不禁止企业发布")
    public ResultVo changePublish(@RequestBody Enterprise enterprise) throws Exception {
        try {
            if (enterpriseService.updateById(enterprise)) {
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


    /**
     * 附件上传
     */
    @PostMapping(value = "/uploadAccessory")
    @ApiOperation(value = "附件上传")
    public ResultVo uploadAccessory(@RequestParam("file") MultipartFile uploadImage, HttpServletRequest
            httpServletRequest) throws IOException, PenintException {
        PenintProperties properties = SpringContextUtil.getBean(PenintProperties.class);
        String requestURL = null;
        JSONObject data = new JSONObject();
//        // 取出上传文件的用户Userid
//        String token = httpServletRequest.getHeader("Authorization");// 从 http 请求头中取出 token
//        Integer userId = Integer.parseInt(JWT.decode(token).getAudience().get(0));
//        log.info("上传该文件的用户userId:" + userId);

        // 计算文件大小
        BigDecimal size = new BigDecimal(uploadImage.getSize());
        BigDecimal mod = new BigDecimal(1024);

        //除一个1024，不保留小数，进行四舍五入
        String fileSize = size.divide(mod).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        log.info("上传文件的大小：" + fileSize + "kb");

        // 生成的文件名
        String fileName = UploadFileUtils.CreateFileUrl(uploadImage.getOriginalFilename());

        // 生成目的文件夹名称（日期命名）
        String folder = UploadFileUtils.CreateNowDate();

        // 原文件名
        String oldName = uploadImage.getOriginalFilename();
        log.info("原文件名：" + oldName);

        // 取出文件的后缀（类型）
        String fileType = oldName.substring(oldName.lastIndexOf(".") + 1);
        log.info("文件名后缀（类型）：" + fileType);

        InputStream inputStream = uploadImage.getInputStream();
        BufferedImage bi = ImageIO.read(inputStream);

        String uploadPath = "";
        if (bi == null) {
            log.info("此文件不是图片文件");
            return ResultVo.failed(201, "此文件不是图片文件");
        } else {
            // 图片请求路径
            requestURL = properties.getResourcePath().getBusinessLicenseUrl() + "/" + folder + "/" + fileName + StringPool.DOT + fileType;
            log.info("图片请求路径：" + requestURL);
            uploadPath = properties.getResourcePath().getUploadBusinessLicense() + "/" + folder;
            log.info("图片上传的路径：" + uploadPath);
        }

        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            uploadImage.transferTo(new File(uploadPath + File.separator + fileName + StringPool.DOT + fileType));
            data.put("status", 200);
            data.put("requestUrl", requestURL);
            log.info("文件或图片上传成功");

        } catch (IOException e) {
            e.printStackTrace();
            log.info("图片上传失败");
            throw new PenintException("图片上传失败");
        }
        return ResultVo.oK(data);
    }
}
