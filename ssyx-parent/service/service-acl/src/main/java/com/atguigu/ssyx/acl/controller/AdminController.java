package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.AdminService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.common.utils.MD5;
import com.atguigu.ssyx.model.acl.Admin;
import com.atguigu.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/admin/acl/user")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    //实现为用户进行分配
    @ApiOperation("为用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long adminId,
                           @RequestParam Long[] roleId){
        roleService.saveAdminRole(adminId,roleId);

        return Result.ok(null);
    }

    //获取所有的角色，根据用户ID查询用户分配的角色列表
    @ApiOperation("获取用户角色")
    @GetMapping("/toAssign/{adminId}")
    public Result toAssign(@PathVariable Long adminId){
        Map<String,Object> map = roleService.getRoleByAdminId(adminId);
        return Result.ok(map);
    }

    //1.用户列表
    @ApiOperation("用户列表")
    @GetMapping("{current}/{limit}")
    public Result getPageList(@PathVariable Long current,
                              @PathVariable Long limit,
                              AdminQueryVo adminQueryVo){
        Page<Admin> pageParam = new Page<>(current,limit);
        IPage<Admin> pageModel = adminService.selectPageUser(pageParam,adminQueryVo);

        return Result.ok(pageModel);
    }

    //2.id查询用户
    @ApiOperation("根据id查询用户")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        Admin admin = adminService.getById(id);
        return Result.ok(admin);
    }
    //3.添加用户
    @ApiOperation("添加用户")
    @PostMapping("/save")
    public Result save(@RequestBody Admin admin){
        String password = admin.getPassword();
        String encrypt = MD5.encrypt(password);
        admin.setPassword(encrypt);
        boolean is_success = adminService.save(admin);
        if (is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //4.修改用户
    @ApiOperation("修改用户")
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin){
        boolean is_success = adminService.updateById(admin);
        if (is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //5 根据id删除用户
    @ApiOperation("根据id删除用户")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean is_success = adminService.removeById(id);
        if (is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //6.批量删除
    @ApiOperation("批量删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean is_success = adminService.removeByIds(idList);
        if (is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
}
