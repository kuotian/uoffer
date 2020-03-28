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
 * 系统登录日志
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("登录日志")
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField(value = "表id")
    private Integer id;

    /**
     * 用户Id
     */
    @ExcelField(value = "用户id")
    private Integer userId;

    /**
     * 用户名
     */
    @ExcelField(value = "用户名")
    private String username;

    /**
     * 登录时间
     */
    @ExcelField(value = "登录时间" ,dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    /**
     * 登录地点
     */
    @ExcelField(value = "登录地点")
    private String address;

    /**
     * IP地址
     */
    @ExcelField(value = "IP地址")
    private String ip;


}
