package com.hxxzt.recruitment.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 简历表
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 简历标题
     */
    private String title;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 参加工作时间
     */
    private LocalDate workingHours;

    /**
     * 现居住地址
     */
    private String nowAddress;

    /**
     * 户籍地址
     */
    private String domicileAddress;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 求职意向
     */
    private String jobIntention;

    /**
     * 学历，本科、大专、高中、初中、小学
     */
    private String education;

    /**
     * 毕业院校
     */
    private String schoolOfGraduation;

    /**
     * 专业
     */
    private String major;

    /**
     * 工作经历
     */
    private String workExperience;

    /**
     * 项目经验
     */
    private String projectExperience;

    /**
     * 自我评价
     */
    private String selfAppraisal;

    /**
     * 刷新简历时间
     */
    private Date refreshTime;

    /**
     * 是否隐藏
     */
    private Boolean isHide;

    /**
     * 是否默认简历
     */
    private Boolean isDefault;

    /**
     * 对应微信Userid
     */
    private Integer wxUserId;

    /**
     * 对应微信昵称
     */
    private String wxNickname;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;

    @TableField(exist = false)
    private ResumeSend resumeSend;


}
