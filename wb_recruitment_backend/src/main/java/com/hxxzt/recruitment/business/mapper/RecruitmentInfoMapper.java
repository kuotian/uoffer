package com.hxxzt.recruitment.business.mapper;

import com.hxxzt.recruitment.business.entity.RecruitmentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 招聘信息 Mapper 接口
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-03
 */
public interface RecruitmentInfoMapper extends BaseMapper<RecruitmentInfo> {

    RecruitmentInfo queryListByRsId(@Param("rsId") Integer rsId);
}
