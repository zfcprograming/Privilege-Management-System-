package com.itlike.service;

import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;
import com.itlike.domain.Role;

import java.util.List;

public interface RoleService {
    public PageListRes getRoles(QueryVo vo);

    void saveRole(Role role);

    void updateRole(Role role);

    void deleteRole(Long rid);

    List<Role> roleList();

    List<Long> getRoleByEid(Long id);
}
