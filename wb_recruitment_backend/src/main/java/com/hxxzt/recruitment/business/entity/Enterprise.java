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
 * 企业信息表
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Enterprise implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 法人姓名
     */
    private String leaderName;

    /**
     * 法人身份证号
     */
    private String idCard;

    /**
     * 统一社会信用代码
     */
    private String uscCode;

    /**
     * 营业执照附件
     */
    private String businessLicense;

    /**
     * 审核状态,true：审核成功，false：审核失败
     */
    private Boolean reviewStatus;

    /**
     * 审核人
     */
    private String reviewPeople;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 对应sys_user表中的Id
     */
    private Integer userId;

    /**
     * 对应username
     */
    private String username;

    /**
     * 是否可以发布招聘信息、宣讲会信息
     */
    private Boolean isPublish;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;


}
