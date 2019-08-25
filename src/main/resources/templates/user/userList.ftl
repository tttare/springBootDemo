<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>用户列表管理</title>
    <#--<#include "/common/common.ftl" >-->
	<#--<#include "/common/WsCommonJS.ftl">-->
</head>

<body class="bgfff">
    <table>
        <thead>
           <tr>
               <th>用户id</th>
               <th>用户姓名</th>
               <th>用户创建时间</th>
           </tr>
            <#list userList as u>
                <tr>
                    <td>${u.userId}</td>
                    <td>${u.userName}</td>
                    <td>${(u.createDate?string("yyyy-MM-dd"))!}</td>
                </tr>
            </#list>
        </thead>

    </table>

</body>

</html>