package com.itlike.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itlike.domain.AjaxRes;
import com.itlike.domain.Employee;
import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;
import com.itlike.service.EmployeeService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class EmployeeController {
    /*注入业务层*/
   @Autowired
   private EmployeeService employeeService;

    @RequestMapping("/employee")
    @RequiresPermissions("employee:index")
    public String employee(){
        return "employee";
    }

    //用来返回json数据
    @RequestMapping("/employeeList")
    @ResponseBody
    public PageListRes employeeList(QueryVo vo){

        /*调用业务层查询员工*/
        PageListRes pageListRes = employeeService.getEmployee(vo);
        return pageListRes;

    }
    /*接收员工添加表单*/
    @RequestMapping("/saveEmployee")
    @ResponseBody
    @RequiresPermissions("employee:add")
    public AjaxRes saveEmployee(Employee employee){

        AjaxRes ajaxRes = new AjaxRes();
        try {
            employee.setState(true);
            /*调用业务层，保存用户*/
            employeeService.saveEmployee(employee);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("保存失败");
        }
        return ajaxRes;

    }
    /*接收更新员工请求*/
    @RequestMapping("/updateEmployee")
    @ResponseBody
    @RequiresPermissions("employee:edit")
    public AjaxRes updateEmployee(Employee employee){

        AjaxRes ajaxRes = new AjaxRes();
        try {
            /*调用业务层，更新员工*/
            employeeService.updateEmployee(employee);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("更新失败");
        }
        return ajaxRes;
    }
    /*接收离职操作请求*/
    @RequestMapping("/updateState")
    @ResponseBody
    @RequiresPermissions("employee:delete")
    public AjaxRes updateState(Long id){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            /*调用业务层，设置员工离职状态*/
            employeeService.updateState(id);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("更新失败");
        }
        return ajaxRes;
    }
    //没有注解，为内部请求
    @ExceptionHandler({AuthorizationException.class})
    public void handleShiroException(HandlerMethod method, HttpServletResponse response) throws Exception{
        /*跳转到一个界面，提示没有权限*/
      /*  判断当前的请求是不是json请求，如果是，返回json给浏览器,让他自己跳转*/
        /*获取方法上的注解*/
        ResponseBody methodAnnotation = method.getMethodAnnotation(ResponseBody.class);
        if(methodAnnotation!=null){
            //Ajax
            AjaxRes ajaxRes = new AjaxRes();
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("你没有权限操作");
            String s=new ObjectMapper().writeValueAsString(ajaxRes);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(s);
        }else{
            response.sendRedirect("nopermission.jsp");
        }

    }
    @RequestMapping("/downloadExcel")
    @ResponseBody
    public void downloadExcel(HttpServletResponse response){
        try {
        //1从数据库当中去列表数据
        QueryVo queryVo=new QueryVo();
        queryVo.setPage(1);
        queryVo.setRows(10);
        PageListRes plr = employeeService.getEmployee(queryVo);
        List<Employee> employees= (List<Employee>)plr.getRows();
        //2 创建Excel 写到Excel当中
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("员工数据");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("用户名");
        row.createCell(2).setCellValue("入职日期");
        row.createCell(3).setCellValue("电话");
        row.createCell(4).setCellValue("邮件");
        HSSFRow employeeRow=null;
        for( int i=0;i<employees.size();i++){
            Employee employee = employees.get(i);
            employeeRow=sheet.createRow(i+1);
            employeeRow.createCell(0).setCellValue(employee.getId());
            employeeRow.createCell(1).setCellValue(employee.getUsername());
            if(employee.getInputtime()!=null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(employee.getInputtime());
                employeeRow.createCell(2).setCellValue(format);

            }else{
                employeeRow.createCell(2).setCellValue("");
            }
            employeeRow.createCell(3).setCellValue(employee.getTel());
            employeeRow.createCell(4).setCellValue(employee.getEmail());

        }
        //3 响应给浏览器

            String filename = new String("员工数据.xls".getBytes("utf-8"), "iso8859-1");
            response.setHeader("content-Disposition","attachment;filename="+filename);
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //下载模板
    @RequestMapping("/downloadExcelTpl")
    @ResponseBody
    public void downloadExcelTpl(HttpServletRequest request, HttpServletResponse response){
        FileInputStream is=null;
        try{
            String filename = new String("ExcelTml.xls".getBytes("utf-8"), "iso8859-1");
            response.setHeader("content-Disposition","attachment;filename="+filename);
            /*获取文件路径*/
            String realPath = request.getSession().getServletContext().getRealPath("static/ExcelTml.xls");
            is=new FileInputStream(realPath);
            IOUtils.copy(is, response.getOutputStream());

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

   //配置文件上传解析器，mvc配置当中
    @RequestMapping("/uploadExcelFile")
    @ResponseBody
    public AjaxRes uploadForm(MultipartFile excel){
        AjaxRes ajaxRes = new AjaxRes();
        try{
            ajaxRes.setMsg("导入成功");
            ajaxRes.setSuccess(true);
            HSSFWorkbook wb = new HSSFWorkbook(excel.getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            //获取最大的行号
            int lastRowNum = sheet.getLastRowNum();
            Row employeeRow=null;
           for(int i=1;i<=lastRowNum;i++){
               employeeRow=sheet.getRow(i);
               Employee employeee = new Employee();
               System.out.println(getCellValue(employeeRow.getCell(0)));
               System.out.println(getCellValue(employeeRow.getCell(1)));
               System.out.println(getCellValue(employeeRow.getCell(2)));
               System.out.println(getCellValue(employeeRow.getCell(3)));
               System.out.println(getCellValue(employeeRow.getCell(4)));

           }

        }catch (Exception e){
            e.printStackTrace();
            ajaxRes.setMsg("导入失败");
            ajaxRes.setSuccess(false);

        }

        return ajaxRes;

    }

    private Object getCellValue(Cell cell){

        switch (cell.getCellType()) {

            case STRING:

                return cell.getRichStringCellValue().getString();

            case NUMERIC:

                if (DateUtil.isCellDateFormatted(cell)) {

                    return cell.getDateCellValue();

                } else {

                    return cell.getNumericCellValue();

                }

            case BOOLEAN:

                return cell.getBooleanCellValue();

            case FORMULA:

                return cell.getCellFormula();

        }

        return cell;

    }





}
