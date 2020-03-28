package com.hxxzt.recruitment.system.service;

import com.hxxzt.recruitment.system.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-20
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    void insertByRoleList(Integer userId, Integer[] roleIds);

    List<Integer>  findUserIdsByRoleId(Integer roleId);

    List<Integer> findUserIdsByMenuId(Integer menuId);

    void updateRoleIdByUserId(Integer roleId, Integer userId);
}
