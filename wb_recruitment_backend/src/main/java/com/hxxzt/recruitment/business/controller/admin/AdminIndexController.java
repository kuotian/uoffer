/**
 * 项目名：recruitment
 * 日  期：2020/3/26
 * 包  名：com.hxxzt.recruitment.business.controller.admin
 *
 * @author： HuangXiuxiang
 */
package com.hxxzt.recruitment.business.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxxzt.recruitment.business.entity.Enterprise;
import com.hxxzt.recruitment.business.entity.WxUser;
import com.hxxzt.recruitment.business.service.ICareerTalkService;
import com.hxxzt.recruitment.business.service.IEnterpriseService;
import com.hxxzt.recruitment.business.service.IRecruitmentInfoService;
import com.hxxzt.recruitment.business.service.IWxUserService;
import com.hxxzt.recruitment.common.vo.ResultVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adminApi/index")
@Slf4j
@Api(tags = "首页相关接口")
public class AdminIndexController {


    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IRecruitmentInfoService recruitmentInfoService;

    @Autowired
    private IEnterpriseService enterpriseService;

    @Autowired
    private ICareerTalkService careerTalkService;

    /**
     * 查询 招聘信息条数
     * 查询 微信用户总数
     * 查询 已认证企业总数
     * 查询 宣讲会信息条数
     */
    @GetMapping
    public ResultVo index() {
        JSONObject data = new JSONObject();
        LambdaQueryWrapper<Enterprise> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Enterprise::getReviewStatus, 1);
        data.put("wxUserCount", wxUserService.count());
        data.put("reCount", recruitmentInfoService.count());
        data.put("enCount", enterpriseService.count(lambdaQueryWrapper));
        data.put("caCount", careerTalkService.count());
        return ResultVo.oK(data);
    }

    @GetMapping("/chart")
    public ResultVo chart() {

        List<WxUser> wxUser = wxUserService.getChart();
        return ResultVo.oK(wxUser);
    }
}
