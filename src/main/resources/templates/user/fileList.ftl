<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>用户列表管理</title>
    <#--<#include "/common/common.ftl" >-->
	<#--<#include "/common/WsCommonJS.ftl">-->
</head>

<body>

    <div id="fileList">

    </div>


</body>
<script src="/js/jquery-1.8.3.js"></script>
<script>
    var fileObject = ${fileObject};
    $(function () {
        //生产数结构
        showFileList(fileObject);
    });
    function showFileList (fileObject) {
        var html ='';
        if(!fileObject.isFile){
            html+= '<div>'+fileObject.name+'</div>'
        }else{
            html+='<a href='+fileObject.path+'>'+fileObject.name+'</a>&nbsp;'
        }
        $("#fileList").append(html);
        if(fileObject.children){
            for (var i=0;i<fileObject.children.length;i++){
                showFileList(fileObject.children[i])
            }
        }
    }

</script>

</html>