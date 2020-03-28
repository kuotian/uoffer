package com.hxxzt.recruitment.business.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 招聘信息
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RecruitmentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公司名
     */
    private String entName;

    /**
     * 职位信息
     */
    private String position;

    /**
     * 任职要求
     */
    private String jobRequirements;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 联系人邮箱
     */
    private String linkmanEmail;

    /**
     * 公司信息
     */
    private String entInfo;

    /**
     * 公司地址
     */
    private String entAddress;

    /**
     * 薪资范围
     */
    private String salary;

    /**
     * 薪资是否面议
     */
    private Boolean isNegotiable;

    /**
     * 发布学校，可不选择
     */
    private String school;

    /**
     * 发布人Id
     */
    private Integer userId;

    /**
     * 发布人账号
     */
    private String username;

    /**
     * 发布时间
     */
    private Date createTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;

    @TableField(exist = false)
    private ResumeSend resumeSend;

    /**
     * 最低薪资
     */
    @TableField(exist = false)
    private String salaryUp;
    /**
     * 最高薪资
     */
    @TableField(exist = false)
    private String salaryDown;


}
