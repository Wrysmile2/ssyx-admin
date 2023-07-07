package com.atguigu.ssyx.acl.service.Impl;

import com.atguigu.ssyx.acl.mapper.RoleMapper;
import com.atguigu.ssyx.acl.service.AdminRoleService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.model.acl.AdminRole;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private AdminRoleService adminRoleService;

    //1.角色列表（条件分页查询）
    @Override
    public IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
        //获取条件值
        String roleName = roleQueryVo.getRoleName();

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        //判断是否为空，不为空封装查询条件
        if (StringUtils.isEmpty(roleName)) {
            wrapper.like(Role::getRoleName, roleName);
        }
        //调用方法实现条件分页查询
        Page<Role> rolePage = baseMapper.selectPage(pageParam, wrapper);

        //返回分页对象
        return rolePage;
    }

    //获取所有的角色，根据用户ID查询用户分配的角色列表
    @Override
    public Map<String, Object> getRoleByAdminId(Long adminId) {
        //1 查询所有角色
        List<Role> allRoleList = baseMapper.selectList(null);

        //2 根据用户id查询用户分配角色列表
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId, adminId);
        List<AdminRole> adminRoleList = adminRoleService.list(wrapper);

        //取出角色id
        List<Long> roleIdList = adminRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());

        List<Role> assignRoleList = new ArrayList<>();
        for(Role role : allRoleList){
            //判断
            if(roleIdList.contains(role.getId())){
                assignRoleList.add(role);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("allRolesList", allRoleList);
        result.put("assignRoles",assignRoleList);
        return result;
    }

    //实现为用户进行分配
    @Override
    public void saveAdminRole(Long adminId, Long[] roleIds) {
        //1 删除用户已经分配过的角色
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId,adminId);
        adminRoleService.remove(wrapper);
        //2 重新分配
        /*for (Long roleId : roleIds) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleService.save(adminRole);
        }*/
        List<AdminRole> list = new ArrayList<>();
        for (Long roleId : roleIds) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            list.add(adminRole);
        }
        //调用方法进行添加
        adminRoleService.saveBatch(list);

    }
}
