package com.itlike.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itlike.domain.Employee;
import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;
import com.itlike.domain.Role;
import com.itlike.mapper.EmployeeMapper;
import com.itlike.service.EmployeeService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    public EmployeeMapper  employeeMapper;
    /*查询员工*/
    public PageListRes getEmployee(QueryVo vo) {
        /*调用mapper 查询员工*/
        Page<Object> page = PageHelper.startPage(vo.getPage(), vo.getRows());
        List<Employee> employees = employeeMapper.selectAll(vo);
        System.out.println(employees);
        /*封装成pageLsit*/
        PageListRes pageListRes = new PageListRes();
        pageListRes.setTotal(page.getTotal());
        pageListRes.setRows(employees);
       /* System.out.println(pageListRes);*/
        return  pageListRes;


    }

   /*保存员工*/
    public void saveEmployee(Employee employee) {
        /*把密码进行加密*/
        Md5Hash md5Hash = new Md5Hash(employee.getPassword(), employee.getUsername(), 2);
        employee.setPassword(md5Hash.toString());
        /*保存员工*/
        employeeMapper.insert(employee);
        /*保存角色*/
        for (Role role :employee.getRoles() ) {
            employeeMapper.insertEmployeeAndRoleRel(employee.getId(),role.getRid());

        }

    }

    /*更新员工*/
    public void updateEmployee(Employee employee) {
        /*打破和角色之间的关系*/
        employeeMapper.deleteRoleRel(employee.getId());
        /*更新员工*/
        employeeMapper.updateByPrimaryKey(employee);
        /*重新建立这个关系*/
        for (Role role :employee.getRoles() ) {
            employeeMapper.insertEmployeeAndRoleRel(employee.getId(),role.getRid());

        }


    }

    /*设置员工离职状态*/
    public void updateState(Long id) {
        employeeMapper.updateState(id);

    }

    @Override
    public Employee getEmployeeWithUserName(String username) {
        return employeeMapper.getEmployeeWithUserName(username);

    }

    @Override
    public List<String> getRolesById(Long id) {

        return employeeMapper.getRolesById(id);
    }

    @Override
    public List<String> getPermissionById(Long id) {
        return employeeMapper.getPermissionById(id);
    }
}
