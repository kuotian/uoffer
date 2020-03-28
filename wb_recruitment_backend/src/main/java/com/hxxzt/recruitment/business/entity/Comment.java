package com.hxxzt.recruitment.business.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 招聘信息或者宣讲会信息，招聘信息：1，宣讲会信息：2
     */
    private Integer type;

    /**
     * 对应宣讲会或者招聘信息id,type为1此字段为招聘信息id，type为2此字段为宣讲会id
     */
    private Integer typeId;

    /**
     * 评论人id
     */
    private Integer wxUserId;

    /**
     * 微信昵称
     */
    private String wxNickname;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;

    @TableField(exist = false)
    private CareerTalk careerTalk;
    @TableField(exist = false)
    private RecruitmentInfo recruitmentInfo;
}
