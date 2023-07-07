package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "角色接口")
@RestController
@RequestMapping("/admin/acl/role")
@CrossOrigin
public class RoleController {

    @Autowired
    private RoleService roleService;

    //1.角色列表（条件分页查询）
    @ApiOperation("角色条件分页查询")
    @GetMapping("{current}/{limit}")
    public Result pageList(@PathVariable Long current,
                           @PathVariable Long limit,
                           RoleQueryVo roleQueryVo){
        //1.创建page对象，传递current，limit
        Page<Role> pageParam = new Page<>(current,limit);
        IPage<Role> pageModel = roleService.selectRolePage(pageParam,roleQueryVo);

        return Result.ok(pageModel);
    }

    //2.根据id获取角色
    @ApiOperation("根据id获取角色")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        Role role = roleService.getById(id);
        return Result.ok(role);
    }

    //3.添加角色
    @ApiOperation("添加角色")
    @PostMapping("/save")
    public Result save(@RequestBody Role role){
        boolean is_success = roleService.save(role);
        if (is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //4.修改角色
    @ApiOperation("修改角色")
    @PutMapping("/update")
    public Result getById(@RequestBody Role role){
        boolean is_success = roleService.saveOrUpdate(role);
        if (is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //5.根据id删除角色
    @ApiOperation("根据id删除角色")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean is_success = roleService.removeById(id);
        if (is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //6.批量删除角色
    @ApiOperation("批量删除角色")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> IdList){
        boolean is_success = roleService.removeByIds(IdList);
        if (is_success){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

}
