# Linux环境服务器的搭建

## linux常用命令

su - root   切换为root用户(centos7:su 切换root su {用户名} 切换到其他用户)

which java  查看jdk安装路径

rm -rf /usr/java/jdk/jdk1.8.0_172/   删除jdk



## linux安装jdk

查看所有jdk版本

``` 
yum -y list java*
```

安装对应版本的jdk

``` 
yum install -y java-1.8.0-openjdk-devel.x86_64
```

安装zip

``` 
yum install zip
```

安装unzip

``` 
yum install unzip
```

jdk的卸载

``` 
which java            //查看jdk安装路径
rm -rf /usr/java/jdk/jdk1.8.0_172/    //删除jdk
```



## linux防火墙设置

查看防火墙状态

**service  iptables status**

暂时关闭防火墙

**service  iptables stop**

永久关闭防火墙

**chkconfig iptables off**

重启防火墙

**service iptables restart**  

重启网络连接

**service network restart**

**5:永久关闭后重启**

**chkconfig iptables on**

## linux ip设置

使用root用户登录进入,打开进去终端

在终端中输入：vi /etc/sysconfig/network-scripts/ifcfg-eth0

:q退出 :wq保存退出