package com.itlike.service.impl;

import com.itlike.domain.Department;
import com.itlike.mapper.DepartmentMapper;
import com.itlike.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentServiceImpl implements DepartmentService {
  /*  注入mapper*/
  @Autowired
  private DepartmentMapper departmentMapper;
    public List<Department> getDepartmentList() {
        return departmentMapper.selectAll();
    }
}
