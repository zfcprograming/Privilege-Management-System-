package com.itlike.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itlike.domain.*;
import com.itlike.mapper.MenuMapper;
import com.itlike.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public PageListRes getMenuList(QueryVo vo) {
        //查询菜单
        Page<Object> page = PageHelper.startPage(vo.getPage(), vo.getRows());
        List<Menu> menus = menuMapper.selectAll();

        /*封装成pageLsit*/
        PageListRes pageListRes = new PageListRes();
        pageListRes.setTotal(page.getTotal());
        pageListRes.setRows(menus);
        /* System.out.println(pageListRes);*/
        return  pageListRes;

    }

    @Override
    //查询出所有的菜单
    public List<Menu> parentList() {
        return menuMapper.selectAll();
    }

    @Override
    public void saveMenu(Menu menu) {
        menuMapper.saveMenu(menu);

    }

    @Override
    public AjaxRes updateMenu(Menu menu) {
        AjaxRes ajaxRes = new AjaxRes();
        /*判断 选择的父菜单是不是自己的子菜单*/
        /*不能设置自己的子菜单为父菜单*/
        Long id = menu.getParent().getId();
        Long parent_id=menuMapper.selectParentId(id);
        if (menu.getId()==parent_id){
            ajaxRes.setMsg("不能设置自己的子菜单为父菜单");
            ajaxRes.setSuccess(false);
            return ajaxRes;
        }
        try {

            menuMapper.updateByPrimaryKey(menu);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("保存失败");
        }
        return ajaxRes;
    }

    @Override
    public AjaxRes deleteMenu(Long id) {

        AjaxRes ajaxRes = new AjaxRes();
        try {
            //先打破关系
            menuMapper.updateMenuRel(id);
            //删除记录
            menuMapper.deleteByPrimaryKey(id);



            ajaxRes.setMsg("删除成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("删除失败");
        }
        return ajaxRes;
    }

    @Override
    public List<Menu> getTreeData() {
        List<Menu> treeData=menuMapper.getTreeData();

        /*判断当前用户有没有对应的权限，如果没有就从集合中移除*/
        Subject subject = SecurityUtils.getSubject();
        Employee employee=(Employee) subject.getPrincipal();
        if(!employee.getAdmin()){
            checkPermission(treeData);
        }
        return treeData;

    }
    public void checkPermission(List<Menu> menus){
        Subject subject = SecurityUtils.getSubject();
        //获取所有的菜单及子菜单
        Iterator<Menu> iterator=menus.iterator();
        while(iterator.hasNext()){
            Menu menu=iterator.next();
            if(menu.getPermission()!=null){
                String presource = menu.getPermission().getPresource();
                if(!subject.isPermitted(presource)){
                    iterator.remove();
                    continue;

                }
            }
            if(menu.getChildren().size()>0){
                checkPermission(menu.getChildren());
            }



        }


    }

}
