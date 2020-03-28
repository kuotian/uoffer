package com.hxxzt.recruitment.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxxzt.recruitment.system.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> findUserRole(@Param("username") String username);

    List<SysRole> findUserRoleByUserId(@Param("userId") Integer userId);

    int countUserRoleByRoleId(Integer roleId);

    SysRole selectRoleById(@Param("roleId") Integer roleId);
}
