package com.hxxzt.recruitment.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxxzt.recruitment.business.entity.ResumeSend;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 简历投递表 Mapper 接口
 * </p>
 *
 * @author niko_hxx
 * @since 2020-03-05
 */
public interface ResumeSendMapper extends BaseMapper<ResumeSend> {

    IPage<ResumeSend> selectByEnterprise(@Param("iPage") IPage<ResumeSend> iPage, @Param("request") QueryRequest request, @Param("name") String name, @Param("major") String major, @Param("userId") Integer userId);

    IPage<ResumeSend> select(@Param("iPage") IPage<ResumeSend> iPage, @Param("request") QueryRequest request, @Param("name") String name, @Param("major") String major);

    List<ResumeSend> queryList(@Param("wxUserId") Integer wxUserId);

    List<ResumeSend> queryListByIsCheck(@Param("wxUserId") Integer wxUserId, @Param("isCheck") Boolean isCheck);

    List<ResumeSend> queryListByStatus(@Param("wxUserId") Integer wxUserId, @Param("status") Integer status);

    ResumeSend isSend(@Param("wxUserId") Integer wxUserId, @Param("infoId") Integer infoId);
}
