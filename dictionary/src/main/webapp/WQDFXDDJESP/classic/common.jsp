<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/7/25 0025
  Time: 下午 7:41
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
        // 辞典类型
        var dicType = "<%=request.getParameter("dicType")%>";
        // 资源类型
        var type = "<%=request.getParameter("type")%>";

        $(document).ready(function () {
            $('#magazineGrid').datagrid({
                //height: 340,
                url: "/get/common/" + type + "/" + dicType,
                method: 'POST',
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
                toolbar: "#tb",
                columns: [[
                    {field: 'id', checkbox: true},
                    {field: 'key', title: '文件名', width: 100, align: 'left'},
                    {field: 'value', title: '内容', width: 200, align: 'left'},
                    {field: 'type', title: '资源类型', width: 40, align: 'left'},
                    {field: 'time', title: '更新时间', width: 60, align: 'left'}
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
                $("#magazineGrid").datagrid("load", {
                    key: $("#key").val(),
                    value: $("#value").val(),
                    operateType: "search"
                });
            });
            $("#edit").click(function () {
                var row = $("#magazineGrid").datagrid("getSelected");
                if (row) {
                    if (row.type == "word") {
                        $.ajax({
                            type: "post",
                            contentType: "application/json",
                            url: "/get/detail",
                            dataType: "json",
                            data: '{"uuid":"' + row.value + '"}',
                            success: function (result) {
                                if (result.detail != null) {
                                    $("#dtDicType").val(dicType);
                                    $("#dtType").val(type);
                                    $("#dtFileName").textbox("setValue", row.key);
                                    $("#uuid").textbox("setValue", row.value);
                                    // json格式化输出
                                    //var json = JSON.parse(result.detail)
                                    //JSON.stringify(json,null,4)
                                    $("#content").textbox("setValue",result.detail);
                                    $("#dialogWord").dialog({closed: false});
                                }
                            },
                            error: function (result) {
                                $.messager.alert('Error', "获取详情数据失败", 'error');
                            },
                            complete: function (result) {
                            }
                        });
                    }else{
                        $("#dicType").val(dicType);
                        $("#type").val(type);
                        $("#id").val(row.id);
                        $("#fileName").textbox("setValue", row.key);
                        $("#csPath").textbox("setValue", row.value);
                        $("#uploadFile").val("");
                        $("#dialogImgAndMp3").dialog({closed: false});
                    }

                }
            });
            $("#classicCancel").click(function () {
                if ("word" == type) {
                    $("#dialogWord").dialog("close")
                }else{
                    $("#dialogImgAndMp3").dialog("close")
                }
            });
        });
    </script>
</head>
<body>
<div id="magazineGrid">
</div>
<div id="tb" style="padding:5px;height:auto">
    <div style="margin-bottom:5px">
        <%--<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" text="添加"></a>--%>
        <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" text="编辑" id="edit"></a>
        <%--<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true"></a>--%>
        <%--<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true"></a>--%>
        <%--<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true"></a>--%>
    </div>
    <div>
        文件名: <input id="key" class="easyui-textbox" style="width:100px;">
        内容: <input id="value" class="easyui-textbox" style="width:180px">
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="search">Search</a>
    </div>
</div>
<div id="dialogImgAndMp3" class="easyui-dialog" title="编辑" modal=true iconCls="icon-edit" closed="true" resizable="true"
     style="display: none;width: 700px;height: 180px;">
    <%--图片/音频上传Form--%>
    <jsp:include page="fileupload.jsp" />
</div>
<div id="dialogWord" class="easyui-dialog" title="编辑" modal=true iconCls="icon-edit" closed="true" resizable="true"
     style="display: none;width: 1100px;height: 850px;">
    <jsp:include page="detail.jsp" />
</div>
<div id="dlg-buttons">
    <%--<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" id="classicCancel">取消</a>--%>
</div>
</body>
</html>
