package com.hxxzt.recruitment.system.controller;


import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.manager.UserManager;
import com.hxxzt.recruitment.common.vo.ResultVo;
import com.hxxzt.recruitment.system.entity.SysMenu;
import com.hxxzt.recruitment.system.service.ISysMenuService;
import com.hxxzt.recruitment.system.service.ISysUserRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@RestController
@RequestMapping("/adminApi/sysMenu")
public class SysMenuController {

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private UserManager userManager;

    @GetMapping("/list")
    @RequiresPermissions("system:menu:view")
    public ResultVo list(SysMenu menu, HttpServletRequest request) {
        Integer userId = JWTUtil.getUserId(request);
        List<SysMenu> menus = menuService.selectMenuListByUserId(menu, userId);
        return ResultVo.oK(menuService.buildMenuTree(menus));
    }

    @PostMapping
    @RequiresPermissions("system:menu:add")
    @Log(module = "菜单管理", operation = "新增菜单")
    public ResultVo add(@RequestBody SysMenu menu) {
        menu.setCreateTime(new Date());
        menuService.save(menu);
        return ResultVo.oK();
    }

    @GetMapping("/{menuId}")
    public ResultVo getInfo(@PathVariable Integer menuId) {
        return ResultVo.oK(menuService.getById(menuId));
    }

    @PutMapping
    @RequiresPermissions("system:menu:edit")
    @Log(module = "菜单管理", operation = "修改菜单")
    public ResultVo edit(@RequestBody SysMenu menu) {
        menu.setModifyTime(new Date());
        menuService.updateById(menu);
        // 查找与这些菜单/按钮关联的用户
        List<Integer> userIds = userRoleService.findUserIdsByMenuId(menu.getMenuId());
        userManager.loadBatchUserRedisCache(userIds);
        return ResultVo.oK();
    }

    @DeleteMapping("/{menuId}")
    @RequiresPermissions("system:menu:remove")
    @Log(module = "菜单管理", operation = "删除菜单")
    public ResultVo delete(@PathVariable Integer menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return ResultVo.failed(201, "存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return ResultVo.failed(201, "菜单已分配,不允许删除");
        }

        // 查找与这些菜单/按钮关联的用户
        List<Integer> userIds = userRoleService.findUserIdsByMenuId(menuId);
        menuService.removeById(menuId);
        userManager.loadBatchUserRedisCache(userIds);

        return ResultVo.oK();
    }
}
