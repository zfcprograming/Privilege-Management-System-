package com.itlike.web;

import com.itlike.domain.AjaxRes;
import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;
import com.itlike.domain.Role;
import com.itlike.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private RoleService roleService;
    @RequestMapping("/role")
    public String employee(){
        return "role";
    }
    @RequestMapping("/getRoles")
    @ResponseBody
    public PageListRes getRoles(QueryVo vo){
        PageListRes roles = roleService.getRoles(vo);
        return roles;

    }
    @RequestMapping("/saveRole")
    @ResponseBody
    public   AjaxRes saveRole(Role role){

        AjaxRes ajaxRes = new AjaxRes();
        try {

            /*调用业务层，保存角色和权限*/
            roleService.saveRole(role);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("保存失败");
        }
        return ajaxRes;

    }
    @RequestMapping("/updateRole")
    @ResponseBody
    public AjaxRes updateRole(Role role){

        AjaxRes ajaxRes = new AjaxRes();
        try {


            roleService.updateRole(role);
            ajaxRes.setMsg("更新角色成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("更新角色失败");
        }
        return ajaxRes;

    }

    @RequestMapping("/deleteRole")
    @ResponseBody
    public  AjaxRes deleteRole(Long rid){
        AjaxRes ajaxRes = new AjaxRes();
        try {


            roleService.deleteRole(rid);
            ajaxRes.setMsg("删除角色成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("删除角色失败");
        }
        return ajaxRes;

    }

    @RequestMapping("/roleList")
    @ResponseBody
    public List<Role> roleList(){
        return roleService.roleList();

    }
    /*根据用户id查询对应的角色*/
    @RequestMapping("/getRoleByEid")
    @ResponseBody
   public List<Long> getRoleByEid(Long id){
      return  roleService.getRoleByEid(id);

    }




}
