<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/1 0001
  Time: 下午 4:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../../js/jquery.form.js"></script>
    <script type="application/javascript">
        $(document).ready(function () {
            $('#formDetail').on("submit", function () {
                $(this).ajaxSubmit({
                    //提交前的回调函数
                    beforeSubmit: function (arr, $form, options) {
                        console.log("beforeSubmit", arr, $form, options)
                    },
                    //提交成功后的回调函数
                    success: function (data, status, xhr, $form) {
                        console.log("success", data, status, xhr, $form);
                        // 对话框图标类型
                        var msgIcon = "";
                        var msgTip = "";
                        switch (data.code) {
                            case 0:
                                msgTip = "Info";
                                msgIcon = "info";
                                break;
                            default:
                                msgTip = "Error";
                                msgIcon = "error";
                        }
                        $.messager.alert(msgTip, data.msg, msgIcon);
                    },
                    error: function (xhr, status, error, $form) {
                        $.messager.alert('Error', "更新失败<br>失败原因:" + error, 'error');
                        console.log("error", xhr, status, error, $form)
                    },
                    complete: function (xhr, status, $form) {
                        console.log("complete", xhr, status, $form)
                    }
                });
                return false; //阻止表单默认提交
            });
        });
    </script>
</head>
<body>
<form id="formDetail" action="/update/detail" method="post" style="margin: 10px;">
    <table style="font-size: 12px;">
        <tr>
            <td>文件名:</td>
            <td>
                <input id="dtFileName" name="dtFileName" class="easyui-textbox" readonly="readonly"/>
                <input id="dtDicType" name="dtDicType" type="hidden"/>
                <input id="dtType" name="dtType" type="hidden"/>
            </td>
        </tr>
        <tr>
            <td>词语ID:</td>
            <td><input name="uuid" id="uuid" class="easyui-textbox" readonly="readonly" style="width: 280px;"/></td>
        </tr>
        <tr>
            <td>详情内容:</td>
            <td>
                <input id="content" name="content" class="easyui-textbox" data-options="multiline:true" style="width:1000px;height:690px">
            </td>
        </tr>
        <tr>
            <td colspan="2" style="width:100%;"><input type="submit" style="margin-top:20px;float:right;" value="更新"></td>
        </tr>
    </table>
</form>
</body>
</html>
