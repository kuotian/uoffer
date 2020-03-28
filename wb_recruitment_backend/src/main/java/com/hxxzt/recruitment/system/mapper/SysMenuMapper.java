package com.hxxzt.recruitment.system.mapper;

import com.hxxzt.recruitment.system.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单 Mapper 接口
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findUserPermissions(@Param("userId") Integer userId);

    List<SysMenu> findUserMenus(@Param("userId") Integer userId);

    List<String> findUserPermissionsByUserId(@Param("userId") Integer userId);

    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Integer userId);

    List<SysMenu> selectMenuListByUserId(@Param("menu") SysMenu menu, @Param("userId") Integer userId);

    List<Integer> selectMenuListByRoleId(@Param("roleId") Integer roleId);

    List<SysMenu> selectMenuList(@Param("menu") SysMenu menu);

}
