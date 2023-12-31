package com.atguigu.ssyx.acl.service.Impl;

import com.atguigu.ssyx.acl.mapper.PermissionMapper;
import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.acl.utils.PermissionHelper;
import com.atguigu.ssyx.model.acl.Permission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    //1 获取权限(菜单/功能)列表
    @Override
    public List<Permission> queryAllPermission() {
        //1 查询所有菜单
        List<Permission> allPermissionList = baseMapper.selectList(null);
        //2 转换要求数据格式
        List<Permission> result = PermissionHelper.buildPermission(allPermissionList);

        return result;
    }

    //4 递归删除菜单
    @Override
    public void removeChildById(Long id) {
        //idList有删除所有菜单id
        List<Long> idList = new ArrayList<>();
        //根据当前菜单的id，获取当前菜单下面的所有的子菜单
        //如果子菜单下面还有子菜单，都要获取到
        //重点：递归找到当前菜单的子菜单
        this.getAllPermissionId(id,idList);
        //设置当前菜单id
        idList.add(id);

        baseMapper.deleteBatchIds(idList);
    }

    //重点：递归找到当前菜单的子菜单
    private void getAllPermissionId(Long id, List<Long> idList) {
        //根据当前菜单id
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPid,id);
        List<Permission> childList = baseMapper.selectList(wrapper);
        //递归查询是否还有子菜单，有 继续查询
        childList.stream().forEach(item->{
            //封装菜单id到idList集合里面
            idList.add(item.getId());
            this.getAllPermissionId(item.getId(),idList);
        });
    }

}
