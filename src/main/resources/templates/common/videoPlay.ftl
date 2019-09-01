<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>视频播放测试</title>
</head>
<body>
    <script type="text/javascript" src="/js/ckplayer.js"></script>
    <div class="video" style="width: 1000px;height: 600px;"></div>
    <script type="text/javascript">
        var videoObject = {
            container: '.video',//“#”代表容器的ID，“.”或“”代表容器的class
            variable: 'player',//该属性必需设置，值等于下面的new chplayer()的对象
            autoplay:true,
            video:'/tttare/桃乃木/HD-ipx-114.mp4'//视频地址
        };
        var player=new ckplayer(videoObject);
    </script>
</body>
</html>