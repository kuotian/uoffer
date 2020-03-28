package com.hxxzt.recruitment.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统字典
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典ID
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    /**
     * 键
     */
    private Long key;

    /**
     * 值
     */
    private String value;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 表名
     */
    private String tableName;


}
