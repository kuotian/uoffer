package com.hxxzt.recruitment.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxxzt.recruitment.business.entity.Resume;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 简历表 Mapper 接口
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-05
 */
public interface ResumeMapper extends BaseMapper<Resume> {

    Resume getResumeByResumeSendId(@Param("resumeSendId") Integer resumeSendId);
}
