package com.itlike.service.impl;

import com.itlike.domain.Permission;
import com.itlike.mapper.PermissionMapper;
import com.itlike.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    public List<Permission> getPermissions() {

       return  permissionMapper.selectAll();
    }


    public List<Permission> getPermissionByRid(Long rid) {


        return permissionMapper.selectPermissionByRid(rid);
    }
}
