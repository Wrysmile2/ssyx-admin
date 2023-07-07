package com.atguigu.ssyx.acl.service;

import com.atguigu.ssyx.model.acl.Permission;

import java.util.List;

public interface PermissionService {

    //1 获取权限(菜单/功能)列表
    List<Permission> queryAllPermission();

    //4 递归删除菜单
    void removeChildById(Long id);

}
