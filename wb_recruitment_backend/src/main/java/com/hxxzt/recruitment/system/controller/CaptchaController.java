package com.hxxzt.recruitment.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.hxxzt.recruitment.common.vo.ResultVo;
import com.hxxzt.recruitment.common.utils.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/adminApi/captchaImage")
public class CaptchaController {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 生成验证码
     */
    @GetMapping
    public ResultVo getCode(HttpServletResponse response) throws IOException {
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        // 唯一标识
        String uuid = IdUtil.simpleUUID();
        String verifyKey = Constant.RM_CAPTCHA_CODE_KEY + uuid;

        redisUtil.setCacheObject(verifyKey, verifyCode, Constant.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 生成图片
        int w = 111, h = 36;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(w, h, stream, verifyCode);
        try {
            JSONObject data = new JSONObject();
            data.put("uuid", uuid);
            data.put("img", Base64.encode(stream.toByteArray()));
            return ResultVo.oK(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVo.failed(201, e.getMessage());
        } finally {
            stream.close();
        }
    }
}