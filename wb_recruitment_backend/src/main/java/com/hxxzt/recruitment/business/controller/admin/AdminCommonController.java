/**
 * 项目名：recruitment
 * 日  期：2020/3/3
 * 包  名：com.hxxzt.recruitment.business.controller.admin
 *
 * @author： niko_hxx
 */
package com.hxxzt.recruitment.business.controller.admin;

import com.hxxzt.recruitment.business.service.ISchoolService;
import com.hxxzt.recruitment.common.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adminApi/common")
@Slf4j
@Api(tags = "公共接口")
public class AdminCommonController {


    @Autowired
    private ISchoolService schoolService;

    /**
     * 查询学校下拉list
     */
    @GetMapping("/getSchoolList")
    @ApiOperation(value = "查询学校下拉list")
    public ResultVo getSchoolList() {
        return ResultVo.oK(schoolService.list());
    }
}