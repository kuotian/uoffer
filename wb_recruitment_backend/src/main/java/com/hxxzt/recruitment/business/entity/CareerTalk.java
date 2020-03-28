package com.hxxzt.recruitment.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 宣讲会表
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CareerTalk implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公司名称
     */
    private String entName;

    /**
     * 宣讲会地址
     */
    private String address;

    /**
     * 宣讲会时间
     */
    private Date time;

    /**
     * 宣讲会内容
     */
    private String content;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 联系人邮箱
     */
    private String linkmanEmail;

    /**
     * 发布学校，可不选择
     */
    private String school;

    /**
     * 发布人id
     */
    private Integer userId;

    /**
     * 发布人用户名
     */
    private String username;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;


}
