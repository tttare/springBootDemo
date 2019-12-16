# Centos7服务器搭建

## 用户创建和切换

``` 
adduser demo_user    //创建一个demo用户
```

```
passwd demo_user       //为用户设置密码
```

新创建的用户并不能使用sudo命令，需要给他添加授权。

**添加sudoers文件可写权限**

``` 
chmod -v u+w /etc/sudoers
```

**编辑sudoers文件**

``` 
vim /etc/sudoers
```

**在文件中 root  ALL=(ALL)     ALL   行后面,换行**

``` 
tttare ALL=(ALL)  ALL//为tttare用户sudoers权限
```

**收回sudoers文件写权限**

``` 
chmod -v u-w /etc/sudoers
```

**用户切换**

``` 
su    //切换到root用户
su  demo_user //切换到非root用户
```

## 文件操作

**创建文件夹**

```
//本目录下创建文件夹
mkdir dir
//创建其他目录下文件夹
mkdir dir/temp
//创建多级目录
mkdir -p temp/dir/aaa
```

**创建文件**

```
touch test   //创建文件与当前目录下
touch temp/test   //创建文件于temp目录下
```

**操作文件**

``` 
// 文件删除  rm
rm -rf test    //删除test文件或文件夹
rm -rf /       //删除服务器所有内容(慎重使用)
//  r代表递归删除  f代表强制不提示

// 文件复制  cp
cp [源文件] [目标文件]

// 文件移动  mv
mv [源文件] [目标文件]  
//由于没有重命名指令,可以用mv实现文件重命名
```



## **编辑文件**vi/vim 的使用

基本上 vi/vim 共分为三种模式，分别是**命令模式（Command mode）**，**输入模式（Insert mode）**和**底线命令模式（Last line mode）**。 这三种模式的作用分别是：

### **命令模式：**

用户刚刚启动 vi/vim，便进入了命令模式。

此状态下敲击键盘动作会被Vim识别为命令，而非输入字符。比如我们此时按下i，并不会输入一个字符，i被当作了一个命令。

以下是常用的几个命令：

- **i** 切换到输入模式，以输入字符。
- **x** 删除当前光标所在处的字符。
- **:** 切换到底线命令模式，以在最底一行输入命令。

若想要编辑文本：启动Vim，进入了命令模式，按下i，切换到输入模式。

命令模式只有一些最基本的命令，因此仍要依靠底线命令模式输入更多命令。

### **输入模式**

在命令模式下按下i就进入了输入模式。

在输入模式中，可以使用以下按键：

- **字符按键以及Shift组合**，输入字符
- **ENTER**，回车键，换行
- **BACK SPACE**，退格键，删除光标前一个字符
- **DEL**，删除键，删除光标后一个字符
- **方向键**，在文本中移动光标
- **HOME**/**END**，移动光标到行首/行尾
- **Page Up**/**Page Down**，上/下翻页
- **Insert**，切换光标为输入/替换模式，光标将变成竖线/下划线
- **ESC**，退出输入模式，切换到命令模式

### **底线命令模式**

在命令模式下按下:（英文冒号）就进入了底线命令模式。

底线命令模式可以输入单个或多个字符的命令，可用的命令非常多。

在底线命令模式中，基本的命令有（已经省略了冒号）：

- q 退出程序
- w 保存文件

按ESC键可随时退出底线命令模式。

## 网络设置

**虚拟机共享主机ip的设置方式**

https://blog.csdn.net/lavendyu/article/details/80373708

**查看ip**

```
ip addr  或者  ifconfig
```

**防火墙操作**

```
执行关闭命令： systemctl stop firewalld.service
再次执行查看防火墙命令：systemctl status firewalld.service
执行开机禁用防火墙自启命令  ： systemctl disable firewalld.service
启动：systemctl start firewalld.service
防火墙随系统开启启动  ： systemctl enable firewalld.service
```



## 服务器环境搭建

### 软件安装常识

**make install** : linux默认安装路劲是/usr/local/目录下.故bin文件都在/usr/local/bin文件夹下被统一管理

**make PREFIX=/usr/local/redis install** :指定文件的安装路劲

###  jdk安装

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

### mysql安装

检查mysql是否安装

```
yum list installed | grep mysql   //检查是否已安装
yum -y remove + 数据库名称   //卸载数据库
```

MySQL依赖libaio安装

``` 
yum search libaio   #检索相关信息
yum install libaio  #安装相关依赖包
```

下载Mysql Yum Repository

```
wget http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm
```

添加Mysql Yum Repository

```
yum localinstall mysql-community-release-el7-5.noarch.rpm
```

查看是否添加成功

``` 
yum repolist enabled | grep "mysql.*-community.*"
```

查看可启用Mysql版本

```
yum repolist all | grep mysql
```

安装mysql

```
yum install mysql-community-server
```

执行

``` 
rpm -qi mysql-community-server.x86_64 0:5.6.24-3.el7
```

安装成功后,启动服务

``` 
which mysql 查看mysql安装的路径
systemctl start mysqld   //开启mysql服务
systemctl status mysqld  //查看mysql状态
```

mysql root账号的设置

``` 
mysql                #进入mysql命令行
mysql -u root -p     
#5.6版本是有密码的 然后百度半天都没找到初始密码文件在哪,不得已,vi /etc/my.cnf在文末加上
skip-grant-tables,即为跳过mysql的验证
免密登录后,按如下语句改的密码（不同版本的改密码语句有不同）
SET PASSWORD FOR 'root'@'localhost' = PASSWORD('MyNewPass');
//该配置要重启mysql服务
sudo service mysql start
sudo service mysql stop
//虚拟机外，访问此数据库的方法，1.修改root账户的访问权限；2.新建一个账号，大家都能访问；任选其一
//1：将root设置为其他主机能访问
update user set host = '%' where user = 'root';
//2：新建一个账号，让此用户能被其他ip访问（上面的root用户对应的是localhost，只能被本机访问）
create user 'tttare'@'%' indentified by 'yourPassword';
grant all on *.* to 'tttare'@'%';
flush privileges;
//现在可以在外部连接虚拟机的数据库了
```

### redis安装

**下载解压安装**

Redis的官方下载网址是：<http://redis.io/download>  (这里下载的是Linux版的Redis源码包

linux下载 wget http://download.redis.io/redis-stable.tar.gz

make指令需要先安装gcc

```
//安装 gcc
yum install -y gcc
//安装 make
yum install -y make
```

安装redis

```
//解压
tar -zxvf  [上传的tar.gz包]
//进入解压后的文件夹，编译make
make
//进入 src文件夹
make install
```

移动redis的配置文件

```
将解压redis包的redis.conf文件移动大/home/tttare/redis/etc文件夹下
将安装后的redis bin(/usr/local/bin) 中的redis-benchmark redis-check-aof redis-cli redis-sentinel redis-server移动到/home/tttare/redis/bin下(cp redis-benchmark redis-check-aof redis-cli redis-sentinel redis-server /home/tttare/redis/bin)
```

修改reids.conf

```
1.注释 bind 127.0.0.1
2.protected-mode 的yes 改为 no
3.daemonize no 改为 yes
```

启动redis

```
redis-server ../etc/redis/conf    //指定配置文件启动
```

### mongoDB安装

我使用的安装包地址：[https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-4.0.0.tgz](https://links.jianshu.com/go?to=https%3A%2F%2Ffastdl.mongodb.org%2Flinux%2Fmongodb-linux-x86_64-4.0.0.tgz)

创建MongoDB文件夹,并解压tgz

```
tar -zxvf mongodb-linux-x86_64-rhel70-4.0.9.tgz;
```

**MongoDB文件夹下创建数据和日志存储的文件夹**

```
mkdir data;
mkdir logs;
# logs文件下 创建mongodb.log日志文件
cd logs; touch mongodb.log; cd ../
# 创建etc并创建MongoDB的配置文件
mkdir etc;
cd etc; touch mongodb.conf
```

**在配置文件mongodb.conf中添加配置**

```
#数据库路径
dbpath=/usr/local/mongodb/data
#日志输出文件路径
logpath=/usr/local/mongodb/logs/mongodb.log
#错误日志采用追加模式
logappend=true
#启用日志文件，默认启用
journal=true
#这个选项可以过滤掉一些无用的日志信息，若需要调试使用请设置为false
quiet=true
#端口号 默认为27017
port=27017
#允许远程访问
bind_ip=0.0.0.0
#开启子进程
fork=true
#开启认证，必选先添加用户
#auth=true
```

**启动MongoDB**

```
./mongod --config ../etc/mongodb.conf
```

添加root用户

```
use admin
db.createUser(
     {
       user:"root",
       pwd:"root",
       roles:[{role:"root",db:"admin"}]
     }
  )
#添加完用户后可以使用show users或db.system.users.find()查看已有用户。
```

关闭mongoDB（可以使用db.shutdownServer()关闭），并使用权限方式再次启动mongoDB。即将配置文件mongodb.conf末尾的auth=true注释放开，保存后再次启动mongoDB服务

验证用户是否添加成功

```
use admin
db.auth("root","root")   #认证，返回1表示成功
```

**编写启动关闭重启shell脚本**

- 进入bin目录创建shell脚本

```
vim mongodb.sh
```

- 编写脚本

```
start() {
/usr/local/mongodb/bin/mongod --config /usr/local/mongodb/etc/mongodb.conf
}
stop() {
/usr/local/mongodb/bin/mongod --config /usr/local/mongodb/etc/mongodb.conf --shutdown
}
#  mongod和mongodb.conf按自己实际安装路径填写
case "$1" in
start)
 start
 ;;

stop)
 stop
 ;;

restart)
 stop
 start
 ;;

*)
 echo $"Usage: $0 {start|stop|restart}"
 exit 1
esac
```

- 修改脚本文件为可执行文件

```
chmod +x mongodb.sh
```

- 验证脚本

```
sh mongodb.sh start
sh mongodb.sh stop
sh mongodb.sh restart
```



### rabbitMQ安装

rabbitmq由erlang语言编写,需要运行在erlang环境下,故需下载erlang,安装并配置环境变量

注意一定要注意rabbitmq与erlang的版本依赖关系,两个版本下载对了就不会有问题,版本关系对应如下:

https://www.rabbitmq.com/which-erlang.html#erlang-repositories

GitHub中rabbitMQ下载列表:https://github.com/rabbitmq/rabbitmq-server/tags

这次选定的版本是rabbitMQ 3.7.18,下载地址:https://github.com/rabbitmq/rabbitmq-server/archive/v3.7.18.tar.gz

erlang版本为21.0.1,下载地址:http://erlang.org/download/otp_src_21.0.tar.gz



### es安装

