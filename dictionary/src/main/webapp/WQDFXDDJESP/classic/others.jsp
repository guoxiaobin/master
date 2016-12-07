<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/3 0003
  Time: 下午 5:17
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
    <script type="text/javascript" src="../../js/common/common.js"></script>
    <script type="text/javascript">
        var dicType = "<%=request.getParameter("dicType")%>"
        $(document).ready(function () {
            $("#outLinePkg").click(function () {
                // 打开遮罩层
                MaskUtil.mask();
                $.ajax({
                    type: "get",
                    url: "/others",
                    data: 'type=0'+'&dicType=' + dicType,
                    success: function (result) {
                        $.messager.alert('Info', result.time + '<br>' + result.msg, 'info');
                    },
                    error: function (result) {
                        $.messager.alert('Error', "处理失败", 'error');
                    },
                    complete: function (result) {
                        // 关闭遮罩层
                        MaskUtil.unmask();
                    }
                });
            });
            $("#androidAppHot").click(function () {
                common(1);
            });
            $("#iosAppHot").click(function () {
                common(2);
            });
            $("#changeChar").click(function () {
                common(3);
            });
            $("#saveTrain").click(function () {
                common(4);
            });
            $("#updImgs").click(function () {
                common(5);
            });
            $("#updMp3s").click(function () {
                common(6);
            });
            $("#updExample").click(function () {
                common(7);
            });
        });

        /**
         * 热修复
         *
         * @param type
         */
        function common(type){
            // 打开遮罩层
            MaskUtil.mask();
            $.ajax({
                type: "get",
                url: "/others",
                data: 'type=' + type + '&dicType=' + dicType,
                headers: { 'DictionarySource': dicType },
                success: function (result) {
                    if(3==type || 4==type || 5==type || 6==type || 7==type){
                        $.messager.alert('Info', result.msg, 'info');
                    }else{
                        $.messager.alert('Info', 'App包类型:'+ result.type + '<br>App版本:' + result.appVersion +'<br>更新包版本:' + result.pkgVersion, 'info');
                    }
                },
                error: function (result) {
                    $.messager.alert('Error', "处理失败", 'error');
                },
                complete: function (result) {
                    // 关闭遮罩层
                    MaskUtil.unmask();
                }
            });
        }
    </script>
</head>
<body>
    <form>
        <div id="tb" style="padding:5px;height:auto">
            <div style="margin-bottom:5px"></br>
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" text="安卓热修复" id="androidAppHot"></a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" text="IOS热修复" id="iosAppHot"></a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" text="离线包打包" id="outLinePkg"></a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" text="全角->半角" id="changeChar"></a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" text="保存练习题数据" id="saveTrain"></a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" text="批量更新图片" id="updImgs"></a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" text="批量更新音频" id="updMp3s"></a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" text="更新凡例" id="updExample"></a>
            </div>
        </div>
    </form>
</body>
</html>
