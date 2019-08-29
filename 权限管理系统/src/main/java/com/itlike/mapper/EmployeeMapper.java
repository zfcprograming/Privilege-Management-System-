package com.itlike.mapper;

import com.itlike.domain.Employee;
import com.itlike.domain.QueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    Employee selectByPrimaryKey(Long id);

    List<Employee> selectAll(QueryVo vo);

    int updateByPrimaryKey(Employee record);

    void updateState(Long id);

    void insertEmployeeAndRoleRel(@Param("id") Long id,@Param("rid") Long rid);

    void deleteRoleRel(Long id);

    Employee getEmployeeWithUserName(String username);


    List<String> getRolesById(Long id);

    List<String> getPermissionById(Long id);
}