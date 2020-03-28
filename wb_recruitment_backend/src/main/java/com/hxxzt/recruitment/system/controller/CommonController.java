package com.hxxzt.recruitment.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxxzt.recruitment.common.authentication.JWTUtil;
import com.hxxzt.recruitment.common.vo.ResultVo;
import com.hxxzt.recruitment.system.entity.SysMenu;
import com.hxxzt.recruitment.system.entity.SysRole;
import com.hxxzt.recruitment.system.entity.SysUser;
import com.hxxzt.recruitment.system.service.ISysMenuService;
import com.hxxzt.recruitment.system.service.ISysRoleService;
import com.hxxzt.recruitment.system.service.ISysUserService;
import com.hxxzt.recruitment.common.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/adminApi/common")
public class CommonController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysUserService userService;

    /**
     * 获取角色下拉框
     *
     * @return 返回id，name
     */
    @GetMapping("/getRole")
    public ResultVo getRole() {
        BaseMapper<SysRole> baseMapper = sysRoleService.getBaseMapper();
        return ResultVo.oK(baseMapper);
    }

    /**
     * 添加角色时获取菜单树
     *
     * @param request
     * @param menu
     * @return
     */
    @GetMapping("/getMenuTree")
    public ResultVo getMenuTree(HttpServletRequest request, SysMenu menu) {
        Integer userId = JWTUtil.getUserId(request);
        List<SysMenu> menus = menuService.selectMenuListByUserId(menu, userId);
        return ResultVo.oK(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeSelect/{roleId}")
    public ResultVo roleMenuTreeSelect(@PathVariable("roleId") Integer roleId, HttpServletRequest request) {
        Integer userId = JWTUtil.getUserId(request);

        List<SysMenu> menus = menuService.selectMenuList(userId);
        JSONObject data = new JSONObject();
        data.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        data.put("menus", menuService.buildMenuTreeSelect(menus));
        return ResultVo.oK(data);
    }


    /**
     * 判断库中用户名 有没有被使用
     *
     * @param username
     * @return
     */
    @GetMapping("/checkUsername/{username}")
    public ResultVo checkUsername(@PathVariable String username) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUsername, username);
        if (userService.getOne(lambdaQueryWrapper) != null) {
            return ResultVo.oK(true);
        } else {
            return ResultVo.oK(false);
        }
    }

    /**
     * 判断请求方法的该用户原密码是否正确
     *
     * @param password
     * @return
     */
    @GetMapping("/checkPassword/{password}")
    public ResultVo checkPassword(@PathVariable String password, HttpServletRequest request) {
        Integer userId = JWTUtil.getUserId(request);
        String username = JWTUtil.getUsername(request);
        String encrypt = MD5Util.encrypt(username, password);// 用户输入的密码加密字符串

        if (userService.getById(userId).getPassword().equalsIgnoreCase(encrypt)) {
            return ResultVo.oK(true);
        } else {
            return ResultVo.oK(false);
        }
    }


}