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
 * 待办事项
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 时间
     */
    private Date time;

    /**
     * 内容
     */
    private String content;

    /**
     * 地址
     */
    private String address;

    /**
     * 微信用户ID
     */
    private Integer wxUserId;

    /**
     * 微信用户昵称
     */
    private String wxNickname;

    private Date createTime;

    private Date modifyTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;

    @TableField(exist = false)
    private String date;

    @TableField(exist = false)
    private String time1;
    @TableField(exist = false)
    private String time2;
}
