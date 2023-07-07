package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.Impl.PermissionServiceImpl;
import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.acl.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/acl/permission")
@CrossOrigin
public class PermissionController {

    @Autowired
    private PermissionServiceImpl permissionService;

    //1 获取权限(菜单/功能)列表
    @ApiOperation("查询所有菜单")
    @GetMapping
    public Result list(){
        List<Permission> list = permissionService.queryAllPermission();
        return Result.ok(list);
    }

    //2 添加菜单
    @ApiOperation("添加菜单")
    @PostMapping("/save")
    public Result save(@RequestBody Permission permission){
        permissionService.save(permission);
        return Result.ok(null);
    }

    //3 修改菜单
    @ApiOperation("修改菜单")
    @PutMapping("/update")
    public Result update(@RequestBody Permission permission){
        permissionService.saveOrUpdate(permission);
        return Result.ok(null);
    }

    //4 递归删除菜单
    @ApiOperation("递归删除菜单")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        permissionService.removeChildById(id);
        return Result.ok(null);
    }
}
