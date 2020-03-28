package com.hxxzt.recruitment.system.controller;


import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.exception.PenintException;
import com.hxxzt.recruitment.common.vo.ResultVo;
import com.hxxzt.recruitment.system.entity.SysOperationLog;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.system.service.ISysOperationLogService;
import com.wuwenze.poi.ExcelKit;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 操作日志表 前端控制器
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@RestController
@RequestMapping("/adminApi/sysOperationLog")
public class SysOperationLogController {

    @Autowired
    private ISysOperationLogService operationLogService;

    @GetMapping("/list")
    @RequiresPermissions("log:operlog:view")
    public ResultVo list(QueryRequest queryRequest, SysOperationLog operationLog) {

        return ResultVo.oK(operationLogService.getList(queryRequest, operationLog));
    }

    /**
     * 删除操作日志
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions("log:operlog:remove")
    @Log(module = "操作日志管理", operation = "删除操作日志")
    public ResultVo remove(@PathVariable Integer[] id) {
        operationLogService.removeByIds(Arrays.asList(id));
        return ResultVo.oK();
    }

    /**
     * 清空操作日志
     *
     * @return
     */
    @DeleteMapping("/clean")
    @RequiresPermissions("log:operlog:clean")
    @Log(module = "操作日志管理", operation = "清空操作日志")
    public ResultVo clean() {
        operationLogService.clean();
        return ResultVo.oK();
    }


    @GetMapping("/export")
    @RequiresPermissions("log:operlog:export")
    public void export(HttpServletResponse response) throws PenintException {
        try {
            List<SysOperationLog> list = operationLogService.selectAll();
            ExcelKit.$Export(SysOperationLog.class, response).downXlsx(list, false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PenintException("导出Excel失败");
        }
    }

    @GetMapping("/getModule")
    public ResultVo getModule(){
        return ResultVo.oK(operationLogService.getModule());
    }
}
