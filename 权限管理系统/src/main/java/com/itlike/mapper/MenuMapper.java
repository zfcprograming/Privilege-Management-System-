package com.itlike.mapper;

import com.itlike.domain.Menu;
import java.util.List;

public interface MenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Menu record);

    Menu selectByPrimaryKey(Long id);

    List<Menu> selectAll();

    int updateByPrimaryKey(Menu record);

    void saveMenu(Menu menu);


    Long selectParentId(Long id);

    void updateMenuRel(Long id);

    List <Menu> getTreeData();
}