package com.atguigu.ssyx.acl.service;

import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface RoleService extends IService<Role> {
    //1.角色列表（条件分页查询）
    IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo);

    //获取所有的角色，根据用户ID查询用户分配的角色列表
    Map<String, Object> getRoleByAdminId(Long adminId);

    //实现为用户进行分配
    void saveAdminRole(Long adminId, Long[] roleId);
}
