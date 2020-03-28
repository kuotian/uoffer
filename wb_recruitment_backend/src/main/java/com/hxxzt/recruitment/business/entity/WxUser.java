package com.hxxzt.recruitment.business.entity;

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
 * 微信小程序用户
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WxUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 微信用户昵称
     */
    private String nickName;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 微信号
     */
    private String wxId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 微信用户头像地址
     */
    private String avatar;

    /**
     * 性别，0：未知，1男，2女
     */
    private Integer gender;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 上次登录Ip
     */
    private String lastLoginIp;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否禁言
     */
    private Boolean isSpeak;

    /**
     * 所属学校
     */
    private String school;

    /**
     * 是否为学生
     */
    private Boolean isStudent;

    /**
     * 账号状态：0锁定，1有效
     */
    private Integer status;

    /**
     * 逻辑删除
     */
    private Boolean deleted;

    @TableField(exist = false)
    private String date;

    @TableField(exist = false)
    private String count;
}
