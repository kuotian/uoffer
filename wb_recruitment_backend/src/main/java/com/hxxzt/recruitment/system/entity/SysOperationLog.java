package com.hxxzt.recruitment.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("操作日志")
public class SysOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField(value = "数据编号")
    private Integer id;

    /**
     * 操作用户
     */
    @ExcelField(value = "操作用户")
    private String username;

    /**
     * 所属模块
     */
    @ExcelField(value = "所属模块")
    private String module;
    /**
     * 操作内容
     */
    @ExcelField(value = "操作内容")
    private String operation;

    /**
     * 耗时
     */
    @ExcelField(value = "耗时(ms)")
    private long time;

    /**
     * 操作方法
     */
    @ExcelField(value = "操作方法")
    private String method;

    /**
     * 方法参数
     */
    @ExcelField(value = "方法参数")
    private String params;

    /**
     * 操作者IP
     */
    @ExcelField(value = "操作者IP")
    private String ip;

    /**
     * 创建时间
     */
    @ExcelField(value = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 返回结果
     */
    @ExcelField(value = "返回结果")
    private String result;

    /**
     * 操作地点
     */
    @ExcelField(value = "操作地点")
    private String address;

    /**
     * 是否操作成功
     */
    @ExcelField(value = "是否操作成功")
    private Boolean isSuccess;


}
