package com.hxxzt.recruitment.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.system.entity.SysRole;
import com.hxxzt.recruitment.system.entity.SysRoleMenu;
import com.hxxzt.recruitment.common.entity.common.QueryRequest;
import com.hxxzt.recruitment.system.mapper.SysRoleMapper;
import com.hxxzt.recruitment.system.service.ISysRoleMenuService;
import com.hxxzt.recruitment.system.service.ISysRoleService;
import com.hxxzt.recruitment.system.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-18
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private ISysRoleMenuService roleMenuService;

    @Override
    public List<SysRole> findUserRole(String username) {
        return sysRoleMapper.findUserRole(username);
    }

    @Override
    public List<SysRole> findUserRoleByUserId(Integer userId) {
        return sysRoleMapper.findUserRoleByUserId(userId);
    }

    @Override
    public IPage<SysRole> queryList(QueryRequest queryRequest, SysRole sysRole) {
        IPage<SysRole> roleIPage = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(sysRole.getRoleName())) {
            lambdaQueryWrapper.like(SysRole::getRoleName, sysRole.getRoleName());
        }
        return sysRoleMapper.selectPage(roleIPage, lambdaQueryWrapper);
    }

    @Override
    public Set<String> selectRolePermissionByUserId(Integer userId) {
        List<SysRole> userRoleByUserId = this.findUserRoleByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : userRoleByUserId) {
            if (perm != null) {
                permsSet.addAll(Arrays.asList(perm.getRoleName().trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public int countUserRoleByRoleId(Integer roleId) {
        return sysRoleMapper.countUserRoleByRoleId(roleId);
    }

    @Override
    public SysRole selectRoleById(Integer roleId) {
        return sysRoleMapper.selectRoleById(roleId);
    }

    @Override
    public void updateRole(SysRole role) {
        // 查找这些角色关联了那些用户

        Integer roleId = role.getRoleId();
        role.setModifyTime(new Date());
        sysRoleMapper.updateById(role);

        // 删除角色菜单关联表中的相关role数据
        LambdaQueryWrapper<SysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        roleMenuService.remove(lambdaQueryWrapper);


        Integer[] menuIds = role.getMenuIds();
        setRoleMenus(roleId, menuIds);
    }

    private void setRoleMenus(Integer roleId, Integer[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setMenuId(Integer.valueOf(menuId));
            rm.setRoleId(roleId);
            roleMenuService.save(rm);
        });
    }

}
