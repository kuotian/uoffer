package com.hxxzt.recruitment.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称/真实姓名
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 最近访问时间
     */
    private String lastLoginIp;

    /**
     * 最近访问时间
     */
    private Date lastLoginTime;

    /**
     * 性别 0男 1女 2保密
     */
    private Integer ssex;

    /**
     * 描述
     */
    private String description;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 状态 0锁定 1有效,默认为1
     */
    private Integer status;

    @TableField(exist = false)
    private String roleId;
    @TableField(exist = false)
    private String roleName;
    @TableField(exist = false)
    private Integer[] roleIds;
}
