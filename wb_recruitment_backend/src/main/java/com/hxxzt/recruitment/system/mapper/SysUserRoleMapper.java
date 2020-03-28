package com.hxxzt.recruitment.system.mapper;

import com.hxxzt.recruitment.system.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-20
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    void insertByRoleList(@Param("userId") Integer userId, @Param("roleIds") Integer[] roleIds);

    List<Integer> findUserIdsByMenuId(@Param("menuId") Integer menuId);

    void updateRoleIdByUserId(@Param("roleId") Integer roleId, @Param("userId") Integer userId);

}
