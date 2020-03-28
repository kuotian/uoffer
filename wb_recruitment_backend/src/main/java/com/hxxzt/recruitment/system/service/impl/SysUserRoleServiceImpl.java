package com.hxxzt.recruitment.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.system.entity.SysUserRole;
import com.hxxzt.recruitment.system.mapper.SysUserRoleMapper;
import com.hxxzt.recruitment.system.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author huangxiuxiang
 * @since 2020-02-20
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public void insertByRoleList(Integer userId, Integer[] roleIds) {
        sysUserRoleMapper.insertByRoleList(userId, roleIds);
    }

    @Override
    public List<Integer> findUserIdsByRoleId(Integer roleId) {
        List<SysUserRole> list = baseMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId));
        return list.stream().map(userRole -> userRole.getUserId()).collect(Collectors.toList());
    }

    @Override
    public List<Integer> findUserIdsByMenuId(Integer menuId) {
        return sysUserRoleMapper.findUserIdsByMenuId(menuId);
    }

    @Override
    public void updateRoleIdByUserId(Integer roleId, Integer userId) {

        sysUserRoleMapper.updateRoleIdByUserId(roleId, userId);
    }
}
