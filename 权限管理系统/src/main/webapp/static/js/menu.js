$(function () {

    $("#menu_datagrid").datagrid({
        url:"/menuList",
        columns:[[
            {field:'text',title:'名称',width:1,align:'center'},
            {field:'url',title:'跳转地址',width:1,align:'center'},
            {field:'parent',title:'父菜单',width:1,align:'center',formatter:function(value,row,index){
                    return value?value.text:'';
                }
            }
        ]],
        fit:true,
        rownumbers:true,
        singleSelect:true,
        striped:true,
        pagination:true,
        fitColumns:true,
        toolbar:'#menu_toolbar'
    });

    /*
     * 初始化新增/编辑对话框
     */
    $("#menu_dialog").dialog({
        width:300,
        height:300,
        closed:true,
        buttons:'#menu_dialog_bt'
    });
    $("#parentMenu").combobox({
        width:150,
        panelHeight:'auto',
        editable:false,
        url:'parentList',
        valueField:'id',//服务器
        textField:'text',//页面
        onLoadSuccess:function () { /*数据加载完毕之后回调*/
            $("#parentMenu").each(function(i){
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if(targetInput){
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
        }
    });

    $("#add").click(function () {
        $("#menu_dialog").dialog("setTitle","添加菜单");
        $("#menu_form").form("clear");
        $("#menu_dialog").dialog("open");
    });
    $("#save").click(function () {
        /*判断当前是添加还是编辑*/
        var id=$("[name='id']").val();
        var url;

        if(id){
            /*编辑*/
            var parent_id=$("[name='parent.id']").val();
            if(id==parent_id){
                $.messager.alert("温馨提示","不能设置自己为父菜单");
                return ;
            }

            url="updateMenu";
        }else{
            /*添加*/
            url="saveMenu";
        }

        $("#menu_form").form("submit",{
            url:url,
            success:function (data) {
                //转成json
                data=$.parseJSON(data);
                if(data.success){
                    $.messager.alert("温馨提示",data.msg);
                    /*关闭对话框*/
                    $("#menu_dialog").dialog("close");
                    $("#parentMenu").combobox("reload");
                    /*重新加载数据表格*/
                    $("#menu_datagrid").datagrid("reload");
                }else{
                    $.messager.alert("温馨提示",data.msg);
                }


            }

        });
    });
    $("#edit").click(function () {
        $("menu_form").form("clear");
        /*获取当前选择的行*/
        var rowData =$("#menu_datagrid").datagrid("getSelected");
        if(!rowData){
            $.messager.alert("提示","选择一行数据进行编辑");
            return;
        }
       /*父菜单回显*/
        if(rowData.parent){
            rowData["parent.id"]=rowData.parent.id;

        }else{
            //回显的placeholder
            $("#parentMenu").each(function (i) {
                var span=$(this).siblings("span")[i];
                var targetInput=$(span).find("input:first");
                if(targetInput){
                    $(targetInput).attr("placeholder",$(this).attr("placeholder"));
                }



            });
        }
        $("#menu_dialog").dialog("setTitle","编辑菜单");
        $("#menu_dialog").dialog("open");
        $("#menu_form").form("load",rowData);



        
    });
    $("#cancel").click(function () {
        $("#menu_dialog").dialog("close");

    });
    $("#del").click(function () {

        /*获取当前选择的行*/
        var rowData =$("#menu_datagrid").datagrid("getSelected");
        if(!rowData){
            $.messager.alert("提示","选择一行数据进行编辑");
            return;
        }
        /*提醒用户，是否做删除操作*/
        $.messager.confirm("确认","是否做删除操作",function (res){
            if(res){
                /*做离职操作*/
                $.get("/deleteMenu?id="+rowData.id,function (data) {
                    if(data.success){
                        $.messager.alert("温馨提示",data.msg);
                        $("#parentMenu").combobox("reload");
                        /*重新加载数据表格*/
                        $("#menu_datagrid").datagrid("reload");
                    }else{
                        $.messager.alert("温馨提示",data.msg);
                    }
                });
            }
        });

    });

});