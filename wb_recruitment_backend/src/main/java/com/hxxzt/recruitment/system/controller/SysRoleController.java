package com.hxxzt.recruitment.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxxzt.recruitment.common.aop.annotation.Log;
import com.hxxzt.recruitment.common.exception.PenintException;
import com.hxxzt.recruitment.common.manager.UserManager;
import com.hxxzt.recruitment.common.vo.ResultVo;
import com.hxxzt.recruitment.system.entity.SysRole;
import com.hxxzt.recruitment.system.entity.SysRoleMenu;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.system.service.ISysRoleMenuService;
import com.hxxzt.recruitment.system.service.ISysRoleService;
import com.hxxzt.recruitment.system.service.ISysUserRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@RestController
@RequestMapping("/adminApi/sysRole")
public class SysRoleController {

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private UserManager userManager;

    /**
     * 查看角色
     * @param queryRequest
     * @param sysRole
     * @return
     */
    @RequiresPermissions("system:role:view")
    @GetMapping("/list")
    public ResultVo list(QueryRequest queryRequest, SysRole sysRole) {

        return ResultVo.oK(roleService.queryList(queryRequest, sysRole));
    }

    /**
     * 新增角色
     * @param sysRole
     * @return
     */
    @PostMapping
    @RequiresPermissions("system:role:add")
    @Log(module = "角色管理", operation = "新增角色")
    public ResultVo addRole(@Validated @RequestBody SysRole sysRole) {
        sysRole.setCreateTime(new Date());
        roleService.save(sysRole);
        Integer[] menuIds = sysRole.getMenuIds();
        setRoleMenus(sysRole, menuIds);

        return ResultVo.oK();
    }


    /**
     * 添加角色数据时，往rolemenu中间表中新增关系
     *
     * @param role
     * @param menuIds
     */
    private void setRoleMenus(SysRole role, Integer[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setMenuId(menuId);
            rm.setRoleId(role.getRoleId());
            sysRoleMenuService.save(rm);
        });
    }

    /**
     * 通过roleId删除角色
     * 已使用的角色禁止删除
     * @param roleId
     * @return
     */
    @DeleteMapping("/{roleIds}")
    @RequiresPermissions("system:role:remove")
    @Log(module = "角色管理", operation = "删除角色")
    public ResultVo deleteByRoleId(@PathVariable("roleIds") Integer roleId) {

        // 判断角色是否被使用
        if (roleService.countUserRoleByRoleId(roleId) > 0) {
            return ResultVo.failed(201, "角色已经被用户绑定，请解除绑定再删除");
        } else {
            // 删除角色
            roleService.removeById(roleId);
            // 删除roleMenu表中关联的数据
            LambdaQueryWrapper<SysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
            sysRoleMenuService.remove(lambdaQueryWrapper);

            return ResultVo.oK();
        }
    }

    /**
     * 根据角色编号获取详细信息
     */
    @GetMapping(value = "/{roleId}")
    @RequiresPermissions("system:role:query")
    public ResultVo getInfo(@PathVariable Integer roleId) {
        return ResultVo.oK(roleService.selectRoleById(roleId));
    }

    /**
     * 修改角色
     */
    @PutMapping
    @RequiresPermissions("system:role:edit")
    @Log(module = "角色管理", operation = "修改角色")
    public ResultVo updateRole(@RequestBody SysRole role) throws PenintException {
        try {
            // 查询角色关联了哪些用户
            List<Integer> userId = userRoleService.findUserIdsByRoleId(role.getRoleId());
            // 修改角色，并重新赋予菜单权限
            roleService.updateRole(role);
            // 重新刷新缓存
            userManager.loadBatchUserRedisCache(userId);
            return ResultVo.oK();
        } catch (Exception e) {
            throw new PenintException("修改角色失败");
        }
    }
}
