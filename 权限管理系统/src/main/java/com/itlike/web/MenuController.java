package com.itlike.web;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itlike.domain.*;
import com.itlike.mapper.MenuMapper;
import com.itlike.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
public class MenuController {
    @Autowired
    private MenuService menuService;
    @RequestMapping("/menu")
    public String employee(){
        return "menu";
    }
    @RequestMapping("/menuList")
    @ResponseBody
    public PageListRes  menuList(QueryVo vo){
        PageListRes pageListRes= menuService.getMenuList(vo);

        return pageListRes;

    }
    //加载父菜单
    @RequestMapping("/parentList")
    @ResponseBody
    public List<Menu> parentList(){
        return menuService.parentList();

    }
    @RequestMapping("/saveMenu")
    @ResponseBody
    public AjaxRes saveMenu(Menu menu){
        AjaxRes ajaxRes = new AjaxRes();
        try {

           menuService.saveMenu(menu);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("保存失败");
        }
        return ajaxRes;

    }
    @RequestMapping("/updateMenu")
    @ResponseBody
    public AjaxRes updateMenu(Menu menu){
        return menuService.updateMenu(menu);
    }
    @RequestMapping("/deleteMenu")
    @ResponseBody
    public AjaxRes deleteMenu(Long id){
        return menuService.deleteMenu(id);
    }
    /*获取树形菜单数据*/
    @RequestMapping("/getTreeData")
    @ResponseBody
    public List<Menu> getTreeData(){


        return menuService.getTreeData();
    }






}
