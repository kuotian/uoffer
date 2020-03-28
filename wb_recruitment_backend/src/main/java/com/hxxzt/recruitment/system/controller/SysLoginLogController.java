package com.hxxzt.recruitment.system.controller;


import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.exception.PenintException;
import com.hxxzt.recruitment.common.vo.ResultVo;
import com.hxxzt.recruitment.system.entity.SysLoginLog;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.system.service.ISysLoginLogService;
import com.wuwenze.poi.ExcelKit;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 系统登录日志 前端控制器
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@RestController
@RequestMapping("/adminApi/loginLog")
public class SysLoginLogController {

    @Autowired
    private ISysLoginLogService loginLogService;

    @GetMapping("/list")
    @RequiresPermissions("log:logininfor:view")
    public ResultVo list(QueryRequest queryRequest, SysLoginLog loginLog) {

        return ResultVo.oK(loginLogService.queryList(queryRequest, loginLog));
    }

    /**
     * 清空登录日志
     *
     * @return
     */
    @DeleteMapping("/clean")
    @RequiresPermissions("log:logininfor:clean")
    @Log(module = "登录日志管理", operation = "清空登录日志")
    public ResultVo clean() {
        loginLogService.clean();
        return ResultVo.oK();
    }

    /**
     * 删除登录日志
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions("log:logininfor:remove")
    @Log(module = "登录日志管理", operation = "删除登录日志")
    public ResultVo remove(@PathVariable Integer[] id) {
        loginLogService.removeByIds(Arrays.asList(id));
        return ResultVo.oK();
    }

    @GetMapping("/export")
    @RequiresPermissions("log:logininfor:export")
    public void export(HttpServletResponse response) throws PenintException {
        try {
            List<SysLoginLog> list = loginLogService.selectAll();
            ExcelKit.$Export(SysLoginLog.class, response).downXlsx(list, false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PenintException("导出Excel失败");
        }
    }
}
