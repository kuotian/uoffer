package com.hxxzt.recruitment.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.system.entity.SysUser;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser findDetail(String username);

    IPage<SysUser> queryList(QueryRequest queryRequest, SysUser user);

    IPage<SysUser> queryFuzz(QueryRequest queryRequest, SysUser user);

    SysUser getInfoById(Integer userId);

    void insertSelective(SysUser sysUser);
}
