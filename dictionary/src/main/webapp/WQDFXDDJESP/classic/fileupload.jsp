<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/7/29 0029
  Time: 下午 5:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../../js/jquery.form.js"></script>
    <script type="application/javascript">
        $(document).ready(function () {
            $('#formImgAndMp3').on("submit", function () {
                $(this).ajaxSubmit({
                    //url: url,                 //默认是form的action
                    //type: 'post',               //默认是form的method（get or post）
                    //dataType: "json",           //html(默认), xml, script, json...接受服务端返回的类型
                    //clearForm: true,          //成功提交后，清除所有表单元素的值
                    //resetForm: true,          //成功提交后，重置所有表单元素的值
                    //target: '#output',          //把服务器返回的内容放入id为output的元素中
                    //timeout: 3000,               //限制请求的时间，当请求大于3秒后，跳出请求
                    //提交前的回调函数
                    beforeSubmit: function (arr, $form, options) {
                        //formData: 数组对象，提交表单时，Form插件会以Ajax方式自动提交这些数据，格式如：[{name:user,value:val },{name:pwd,value:pwd}]
                        //jqForm:   jQuery对象，封装了表单的元素
                        //options:  options对象
                        //比如可以再表单提交前进行表单验证
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
                        $.messager.alert('Error', "请选择要上传的文件", 'error');
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
<form id="formImgAndMp3" action="/uploadOne" method="post" style="margin: 10px;" enctype="multipart/form-data">
    <table style="font-size: 12px;">
        <tr>
            <td>文件名:</td>
            <td>
                <% if("0".equals(request.getSession().getAttribute("role"))){ %>
                    <input id="fileName" name="fileName" class="easyui-textbox"/>
                <% } else{ %>
                    <input id="fileName" name="fileName" class="easyui-textbox" readonly="readonly"/>
                <% } %>
                <input id="id" name="id" type="hidden"/>
                <input id="dicType" name="dicType" type="hidden"/>
                <input id="type" name="type" type="hidden"/>
            </td>
        </tr>
        <tr>
            <td>CS路径:</td>
            <td>
                <% if("0".equals(request.getSession().getAttribute("role"))){ %>
                    <input name="csPath" id="csPath" class="easyui-textbox" style="width: 600px;"/>
                <% } else{ %>
                    <input name="csPath" id="csPath" class="easyui-textbox" readonly="readonly" style="width: 600px;"/>
                <% } %>
            </td>
        </tr>
        <tr>
            <td>本地路径:</td>
            <td><input type="file" id="uploadFile" name="uploadFile"/></td>
        </tr>
        <tr>
            <td colspan="2" style="width:100%;"><input type="submit" style="margin-top:20px;float:right;" value="更新"></td>
        </tr>
    </table>
</form>
</body>
</html>
