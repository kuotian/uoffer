package com.hxxzt.recruitment.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxxzt.recruitment.system.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
public interface ISysRoleService extends IService<SysRole> {

    List<SysRole> findUserRole(String username);

    List<SysRole> findUserRoleByUserId(Integer userId);

    IPage<SysRole> queryList(QueryRequest queryRequest, SysRole sysRole);

    Set<String> selectRolePermissionByUserId(Integer userId);

    /**
     * 通过角色ID查询角色使用数量
     * @param roleId
     * @return
     */
    int countUserRoleByRoleId(Integer roleId);

    SysRole selectRoleById(Integer roleId);

    void updateRole(SysRole role);
}
