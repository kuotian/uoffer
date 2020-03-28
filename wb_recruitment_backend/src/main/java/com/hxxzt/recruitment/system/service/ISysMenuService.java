package com.hxxzt.recruitment.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxxzt.recruitment.system.entity.SysMenu;
import com.hxxzt.recruitment.common.entity.common.TreeSelect;
import com.hxxzt.recruitment.common.entity.router.RouterVo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> findUserPermissions(Integer userId);

    List<SysMenu> findUserMenus(Integer userId);

    Set<String> findUserPermissionsByUserId(Integer userId);


    List<SysMenu> selectMenuTreeByUserId(Integer userId);

    List<RouterVo> buildMenus(List<SysMenu> menuList);

    List<SysMenu> selectMenuListByUserId(SysMenu menu, Integer userId);

    List<SysMenu> buildMenuTree(List<SysMenu> menus);

    List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    List<SysMenu> selectMenuList(Integer userId);

    List<Integer> selectMenuListByRoleId(Integer roleId);

    /**
     * 查询菜单是否有角色使用
     * @param menuId
     * @return
     */
    boolean checkMenuExistRole(Integer menuId);

    /**
     * 是否存在菜单子节点
     */
    boolean hasChildByMenuId(Integer menuId);

}
