package com.hxxzt.recruitment.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxxzt.recruitment.system.entity.SysUser;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser findDetail(@Param("username") String username);

    IPage<SysUser> queryFuzz(IPage<SysUser> userIPage, @Param("queryRequest") QueryRequest queryRequest, @Param("user") SysUser user);

    SysUser getInfoById(@Param("userId") Integer userId);

    void insertSelective(SysUser sysUser);
}
