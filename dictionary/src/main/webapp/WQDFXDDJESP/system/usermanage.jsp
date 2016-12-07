<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/5 0005
  Time: 下午 2:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="../../js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="../../js/easyui/themes/icon.css">
    <script type="text/javascript" src="../../js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="../../js/easyui/jquery.easyui.min.js"></script>
    <script type="application/javascript">
        $(document).ready(function () {
            $('#userDataGrid').datagrid({
                //height: 340,
                url: "/get/userinfo",
                method: 'GET',
                <%--queryParams: { 'type': <%=request.getParameter("type")%>},--%>
                idField: 'id',
                striped: true,     // 隔行变色
                fitColumns: true,
                singleSelect: true,// 设定单选
                rownumbers: true,
                pagination: true,  // 是否分页
                nowrap: false,     // 是否换行显示
                pageSize: 50,       // 每页显示行数
                pageList: [10, 20, 50, 100, 150, 200], // 每页可显示行数列表
                showFooter: true,
                iconCls: 'icon-ok',
                toolbar: "#userTb",
                columns: [[
                    {field: 'id', checkbox: true},
                    {field: 'username', title: '用户名', width: 100, align: 'left'},
                    {field: 'rolename', title: '角色', width: 200, align: 'left'}
//                    {field: 'role', title: '角色', width: 40, align: 'left'}
                ]],
                onBeforeLoad: function (param) {
                },
                onLoadSuccess: function (data) {

                },
                onLoadError: function () {

                },
                onClickCell: function (rowIndex, field, value) {

                }
            });
            $("#search").click(function () {
                $("#userDataGrid").datagrid("load", {
                    key: $("#key").val(),
                    value: $("#value").val(),
                    operateType: "search"
                });
            });
        });
    </script>
</head>
<body>
    <div id="userDataGrid">
    </div>
    <div id="userTb" style="padding:5px;height:auto">
        <div style="margin-bottom:5px">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" text="添加"></a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" text="编辑" id="edit"></a>
        </div>
        <div>
            用户名: <input id="key" class="easyui-textbox" style="width:100px;">
            角色: <input id="value" class="easyui-textbox" style="width:180px">
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="search">Search</a>
        </div>
    </div>
</body>
</html>
