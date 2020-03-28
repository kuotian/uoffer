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
 * 简历投递表
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ResumeSend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 简历Id
     */
    private Integer resumeId;

    /**
     * 招聘信息Id
     */
    private Integer infoId;

    /**
     * 是否查看
     */
    private Boolean isCheck;

    /**
     * 查看时间
     */
    private Date checkTime;

    /**
     * 1有意向，2不合适，3面试邀请
     */
    private Integer status;

    /**
     * 面试时间
     */
    private Date interviewTime;


    /**
     * 面试邀请状态，0：未回复，1：已接受
     */
    private Integer interviewStatus;

    private Date createTime;

    @TableField(exist = false)
    private Resume resume;
    @TableField(exist = false)
    private RecruitmentInfo recruitmentInfo;

    @TableField(exist = false)
    private Integer rsId;
}
