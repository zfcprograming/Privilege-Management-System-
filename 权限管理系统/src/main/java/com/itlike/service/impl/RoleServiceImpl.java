package com.itlike.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itlike.domain.PageListRes;
import com.itlike.domain.Permission;
import com.itlike.domain.QueryVo;
import com.itlike.domain.Role;
import com.itlike.mapper.RoleMapper;
import com.itlike.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public PageListRes  getRoles(QueryVo vo) {
        /*调用mapper，查询数据*/
        Page<Object> page = PageHelper.startPage(vo.getPage(), vo.getRows());
        List<Role> roles = roleMapper.selectAll();
        PageListRes pageListRes = new PageListRes();
        pageListRes.setTotal(page.getTotal());
        pageListRes.setRows(roles);
        return pageListRes;


    }


    public void saveRole(Role role) {

        //1保存角色
        roleMapper.insert(role);
        //rid 3 pid 1 ,2
        //2保存角色和权限之间关系
      for (Permission permission:role.getPermissions()){
          roleMapper.insertRoleAndPermissionRel(role.getRid(),permission.getPid());
      }


    }

    @Override
    public void updateRole(Role role) {
        /*先去打破权限和角色之前之间的关系*/
        roleMapper.deletePermissionRel(role.getRid());
        /*更新角色*/
        roleMapper.updateByPrimaryKey(role);
        /*重新保存建立与权限的关系*/
        for (Permission permission:role.getPermissions()){
            roleMapper.insertRoleAndPermissionRel(role.getRid(),permission.getPid());
        }
    }

    @Override
    public void deleteRole(Long rid) {
        /*删除关联的权限*/
        roleMapper.deletePermissionRel(rid);
        /*删除对应的角色*/
        roleMapper.deleteByPrimaryKey(rid);
    }

    @Override
    public List<Role> roleList() {
        return roleMapper.selectAll();
    }

    @Override
    public List<Long> getRoleByEid(Long id) {
        return roleMapper.getRoleWithId(id);
    }
}
