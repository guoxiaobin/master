<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/7/19 0019
  Time: 上午 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>电子辞书后台管理系统</title>
    <link rel="stylesheet" type="text/css" href="../js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="../js/easyui/themes/icon.css">
    <style>
        #center{
            position:absolute;
            width: 400px;
            height: 110px;
            left: 50%;
            top: 50%;
            margin: -60px 0px 0px -60px;
        }
    </style>
    <script type="text/javascript" src="../js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../js/jquery.form.js"></script>
    <script type="text/javascript" src="../js/common/common.js"></script>
    <script type="application/javascript">
        $(document).ready(function () {
            $('#login').click(function () {
                if(!check()){
                    $.messager.alert("Info", "用户名和密码不能为空", "info");
                    return;
                }

                var pwd = encode64($('#password').val());
                $.ajax({
                    type: "post",
                    contentType: "application/json",
                    url: "/check",
                    dataType: "json",
                    data: '{"username":"' + $('#username').val() + '","pwd":"' + pwd + '"}',
                    success: function (result) {
                        // 对话框图标类型
                        var msgIcon = "";
                        var msgTip = "";
                        switch (result.code) {
                            case 0:
                                window.location.href = "/WXERSDP01-ERDFDSFRINDEX";
                                break;
                            default:
                                msgTip = "Error";
                                msgIcon = "error";
                                $.messager.alert(msgTip, result.msg, msgIcon);
                        }
                    },
                    error: function (result) {
                        $.messager.alert('Error', "系统出错，请联系管理员", 'error');
                    },
                    complete: function (result) {
                    }
                });
            });
        });

        function check(){
            var username = $("#username").val();
            var pwd = $("#password").val();
            if(username == "" || pwd == ""){
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<form>
    <div id="center">
        <table>
            <tr>
                <td style="font-size: 15px;text-align: right;">用户名：</td>
                <td><input type="text" id="username" name="username"></td>
            </tr>
            <tr>
                <td  style="font-size: 15px;text-align: right;">密码：</td>
                <td><input type="password" id="password" name="password"></td>
            </tr>
            <tr>
                <td colspan="2">
                    <div style=" float: right;">
                        <input type="button" id="login" value="登录">
                        <input type="reset" id="reset" value="重置">
                    </div>
                </td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>
