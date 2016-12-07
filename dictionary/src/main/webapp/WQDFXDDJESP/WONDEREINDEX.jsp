<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/7/19 0019
  Time: 下午 3:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>电子辞书后台管理系统</title>
    <link rel="stylesheet" type="text/css" href="../js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="../js/easyui/themes/icon.css">
    <script type="text/javascript" src="../js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript">
        function showRight(url){
            $('#iContent').attr("src",url);
        }
    </script>
</head>
<body>
<div class="easyui-layout" style="width:100%;height:100%;">
    <div region="north" split="true" style="width: auto;height: 110px;">
        <span style="font-size: 50px;font-family: 'Baskerville Old Face';">电子辞书后台管理系统</span>
    </div>
    <div region="west" split="true" title="Menus" style="width:200px;">
        <ul class="easyui-tree">
            <li>
                <span>电子辞书</span>
                <ul>
                    <c:forEach var="d" items="${dicMap}">
                        <li>
                            <span>${d.dicName}</span>
                            <ul>
                                <c:forEach var="menu" items="${d.dictionaryMenusList}">
                                    <li><a href="javascript:void(0)" onclick="showRight('${menu.url}')">${menu.menuName}</a></li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>
            </li>
            <% if("0".equals(request.getSession().getAttribute("role"))){ %>
            <li>
                <span>系统管理</span>
                <ul>
                    <li><a href="javascript:void(0)" onclick="showRight('../WQDFXDDJESP/system/usermanage.jsp')">用户管理</a></li>
                </ul>
            </li>
            <% } %>

        </ul>
    </div>
    <div id="content" region="center" title="Content" style="padding:5px;">
        <iframe id="iContent" frameborder="0" style="width:100%;height: 100%;"></iframe>
    </div>
</div>
</body>
</html>
