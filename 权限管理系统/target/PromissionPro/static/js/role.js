$(function () {
    $("#role_dg").datagrid({
        url:"/getRoles",
        columns:[[
            {field:'rnum',title:'角色编号',width:100,align:'center'},
            {field:'rname',title:'角色名称',width:100,align:'center'}


        ]],
        fit:true,
        fitColumns:true,
        rownumbers:true,
        pagination:true,
        singleSelect:true,
        striped:true,
        toolbar:"#toolbar"



    });
    $("#dialog").dialog({
        width:600,
        height:500,
        buttons:[{
            text:'保存',
            handler:function () {
                /*判断当前是保存操作还是编辑操作*/
              var rid=  $("[name='rid']").val();
              var url;
              if(rid){
                  url="updateRole"
              }else{
                  url="saveRole";
              }
                $("#myform").form("submit",{
                    url:url,
                    onSubmit:function(param){
                    var allRows=  $("#role_data2").datagrid("getRows");
                    for( var i=0;i<allRows.length;i++){
                        var row=allRows[i];
                        param["permissions["+i+"].pid"]=row.pid;
                    }


                    },
                    success:function (data) {
                        data=$.parseJSON(data);
                        if(data.success){
                            $.messager.alert("温馨提示",data.msg);
                            /*关闭对话框*/
                            $("#dialog").dialog("close");
                            /*重新加载数据表格*/
                            $("#role_dg").datagrid("reload");
                        }else{
                            $.messager.alert("温馨提示",data.msg);
                        }

                    }
                });

            }

        },{
            text:'关闭',
            handler:function () {
                $("#dialog").dialog("close");

            }
        }],
        closed:true

    });

    $("#add").click(function () {
        $("#myform").form("clear");
        $("#role_data2").datagrid("loadData",{rows:[]});
        $("#dialog").dialog("setTitle","添加角色");


        $("#dialog").dialog("open");



    });
    $("#role_data1").datagrid({
        title:"所有权限",
        width:250,
        height:400,
        singleSelect:true,
        fitColumns:true,
        url:'/permissionList',
        columns:[[
            {field:'pname',title:'权限名称',width:100,align:'center'}



        ]],
        onClickRow:function (rowIndex,rowData) {
           var allRows= $("#role_data2").datagrid("getRows");
           for(var i=0;i<allRows.length;i++){
               var row=allRows[i]
               if(rowData.pid==row.pid){
                var index= $("#role_data2").datagrid("getRowIndex",row);
                   $("#role_data2").datagrid("selectRow",index);
                   return ;
               }

           }
            $("#role_data2").datagrid("appendRow",rowData);

        }

    });
    $("#role_data2").datagrid({
        title:"已选权限",
        width:250,
        height:400,
        singleSelect:true,
        fitColumns:true,
        columns:[[
            {field:'pname',title:'权限名称',width:100,align:'center'}



        ]],
        onClickRow:function (rowIndex,rowData) {
            $("#role_data2").datagrid("deleteRow",rowIndex);
        }

    });
    $("#edit").click(function () {
        /*获取当前选择的行*/
        var rowData =$("#role_dg").datagrid("getSelected");
        console.log(rowData);
        if(!rowData){
            $.messager.alert("提示","选择一行数据进行编辑");
            return;
        }
        /*加载当前角色下的权限*/
        var options=$("#role_data2").datagrid("options");
        options.url="/getPermissionByRid?rid="+rowData.rid;
        /*重新加载数据*/
        $("#role_data2").datagrid("load");
        $("#myform").form("load",rowData);
        $("#dialog").dialog("setTitle","编辑角色");

        $("#dialog").dialog("open");

    });
    $("#remove").click(function () {
        /*获取当前选择的行*/
        var rowData =$("#role_dg").datagrid("getSelected");
        console.log(rowData);
        if(!rowData){
            $.messager.alert("提示","选择一行数据进行编辑");
            return;
        }
        /*get请求不用转json*/
        $.get("deleteRole?rid="+rowData.rid,function (data) {
            if(data.success){
                $.messager.alert("温馨提示",data.msg);

                /*重新加载数据表格*/
                $("#role_dg").datagrid("reload");
            }else{
                $.messager.alert("温馨提示",data.msg);
            }

        });

    });
});