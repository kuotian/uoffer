package com.hxxzt.recruitment.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.common.manager.UserManager;
import com.hxxzt.recruitment.system.entity.SysMenu;
import com.hxxzt.recruitment.system.entity.SysRoleMenu;
import com.hxxzt.recruitment.common.entity.common.TreeSelect;
import com.hxxzt.recruitment.common.entity.router.MetaVo;
import com.hxxzt.recruitment.common.entity.router.RouterVo;
import com.hxxzt.recruitment.system.mapper.SysMenuMapper;
import com.hxxzt.recruitment.system.service.ISysMenuService;
import com.hxxzt.recruitment.system.service.ISysRoleMenuService;
import com.hxxzt.recruitment.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ISysRoleMenuService roleMenuService;

    @Override
    public List<SysMenu> findUserPermissions(Integer userId) {
        return sysMenuMapper.findUserPermissions(userId);
    }

    @Override
    public List<SysMenu> findUserMenus(Integer userId) {
        return sysMenuMapper.findUserMenus(userId);
    }

    @Override
    public Set<String> findUserPermissionsByUserId(Integer userId) {
        Set<String> roles = new HashSet<String>();
        roles.addAll(sysMenuMapper.findUserPermissionsByUserId(userId));
        return roles;

    }

    @Override
    public List<SysMenu> selectMenuTreeByUserId(Integer userId) {
        List<SysMenu> menuList = sysMenuMapper.selectMenuTreeByUserId(userId);
        List<SysMenu> childPerms = getChildPerms(menuList, 0);
        return childPerms;
    }

    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menuList) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menuList) {
            RouterVo router = new RouterVo();
            router.setName(StringUtils.capitalize(menu.getPath()));
            router.setPath(getRouterPath(menu));
            router.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && menu.getType() == 0) {
//                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<SysMenu> selectMenuListByUserId(SysMenu menu, Integer userId) {
        if (userManager.isAdmin(userId)) {
            return sysMenuMapper.selectMenuList(menu);
        } else {
            return sysMenuMapper.selectMenuListByUserId(menu, userId);
        }
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == 0) {
                recursionFn(menus, t);
                returnList.add(t);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    @Override
    public List<SysMenu> selectMenuList(Integer userId) {
        return selectMenuList(new SysMenu(), userId);
    }

    @Override
    public List<Integer> selectMenuListByRoleId(Integer roleId) {
        return sysMenuMapper.selectMenuListByRoleId(roleId);
    }

    /**
     * select count(1) from sys_role_menu where menu_id = #{menuId}
     *
     * @param menuId
     * @return
     */
    @Override
    public boolean checkMenuExistRole(Integer menuId) {
        LambdaQueryWrapper<SysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoleMenu::getMenuId, menuId);
        int count = roleMenuService.count(lambdaQueryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * select count(1) from sys_menu where parent_id = #{menuId}
     *
     * @param menuId
     * @return
     */
    @Override
    public boolean hasChildByMenuId(Integer menuId) {
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysMenu::getParentId, menuId);
        int count = sysMenuMapper.selectCount(lambdaQueryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(SysMenu menu, Integer userId) {

        menu.getParams().put("userId", userId);
        if (userManager.isAdmin(userId)) {
            return sysMenuMapper.selectMenuList(menu);
        } else {
            return sysMenuMapper.selectMenuListByUserId(menu, userId);
        }
    }


    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录
        if (0 == menu.getParentId() && !menu.getIsLink()) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<SysMenu> it = childList.iterator();
                while (it.hasNext()) {
                    SysMenu n = (SysMenu) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
