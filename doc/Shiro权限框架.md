# Shiro权限框架

## 简介

​		Apache Shiro是一个强大且易用的Java安全框架,执行身份验证、授权、密码和会话管理。使用Shiro的易于理解的API,您可以快速、轻松地获得任何应用程序,从最小的移动应用程序到最大的网络和企业应用程序。

## 核心核心组件：Subject, SecurityManager 和 Realms.

​		**Subject**：即“当前操作用户”。但是，在Shiro中，Subject这一概念并不仅仅指人，也可以是第三方进程、后台帐户（Daemon Account）或其他类似事物。它仅仅意味着“当前跟软件交互的东西”。
Subject代表了当前用户的安全操作，SecurityManager则管理所有用户的安全操作。
　　**SecurityManager**：它是Shiro框架的核心，典型的Facade(**外观模式**)模式，Shiro通过SecurityManager来管理内部组件实例，并通过它来提供安全管理的各种服务。
　　**Realm**： Realm充当了Shiro与应用安全数据间的“桥梁”或者“连接器”。也就是说，当对用户执行认证（登录）和授权（访问控制）验证时，Shiro会从应用配置的Realm中查找用户及其权限信息。
　　从这个意义上讲，Realm实质上是一个安全相关的DAO：它封装了数据源的连接细节，并在需要时将相关数据提供给Shiro。当配置Shiro时，你必须至少指定一个Realm，用于认证和（或）授权。配置多个Realm是可以的，但是至少需要一个。
　　Shiro内置了可以连接大量安全数据源（又名目录）的Realm，如LDAP、关系数据库（JDBC）、类似INI的文本配置资源以及属性文件等。如果缺省的Realm不能满足需求，你还可以插入代表自定义数据源的自己的Realm实现。

### 外观模式

​		Facade(外观模式)是为了解决类与类之家的依赖关系的，像spring一样，可以将类和类之间的关系配置到配置文件中，而外观模式就是将他们的关系放在一个Facade类中，降低了类类之间的耦合度，该模式中没有涉及到接口，看下类图

![](img\20180805165243223.png)

将CPU,Memory,Disk的功能startup(),shutdown()封装在Computer中,减少User依赖的类数量;如果我们没有Computer类，那么，CPU、Memory、Disk他们之间将会相互持有实例，产生关系，这样会造成严重的依赖，修改一个类，可能会带来其他类的修改，这不是我们想要看到的，有了Computer类，他们之间的关系被放在了Computer类里，这样就起到了解耦的作用，这，就是外观模式！

## **用户名密码身份认证流程**

![](img\1aab496903c24a2d97d24181bd3b8dad.jpeg)

身份验证代码展示:

用户登录

``` java
// 1、获取Subject实例对象
Subject currentUser = SecurityUtils.getSubject();
// 2、将用户名和密码封装到UsernamePasswordToken
UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
// 4、认证
try {
    currentUser.login(token);// 传到MyAuthorizingRealm类中的方法进行认证
    Session session = currentUser.getSession();
    session.setAttribute("userName", userName);
    loginResult.setLogin(true);
    return loginResult;
}catch (UnknownAccountException e)
{
    e.printStackTrace();
    msg = "账号不存在!";
}
catch (IncorrectCredentialsException e)
{
    msg = "密码不正确!";
}
catch (AuthenticationException e) {
    e.printStackTrace();
    msg="用户验证失败!";
}
```

用户登出

``` java
public void logout() {
    Subject subject = SecurityUtils.getSubject();
    subject.logout();
}
```

用户过期设置

``` java
//永不过期,在登陆最开始加上(负值永不过期)
SecurityUtils.getSubject().getSession().setTimeout(-1000L);
//timeout中的时间单位为ms，但是Shiro会把这个时间转成:s，而且是会舍掉小数部分，这样我设置的是-1ms，转成s
//后就是0s，马上就过期了。所有要是除以1000以后还是负数，必须设置小于-1000

//其他时间 单位毫秒
SecurityUtils.getSubject().getSession().setTimeout(1800000);
```



继承AuthorizingRealm的MyAuthorizingRealm中,关于身份验证的代码

``` java
protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
    log.info("MyShiroRealm.doGetAuthenticationInfo()");
    //获取用户的输入的账号.
    String userName = (String)token.getPrincipal();
    log.info("----"+token.getCredentials());
    //通过username从数据库中查找 User对象.
    //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
    User user = userService.findByUserName(userName);
    log.info("----->>user="+user);
    if(user == null){
        return null;
    }
    SimpleAuthenticationInfo authenticationInfo;
    try{
        authenticationInfo = new SimpleAuthenticationInfo(
            user, //这里传入的是user对象，比对的是用户名，直接传入用户名也没错，但是在授权部分就需要自己重新从数据库里取权限
            user.getPassword(), //密码
            ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
            getName()  //realm name
        );
        //登录成功,将用户信息存入Session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute("userName", userName);
        session.setAttribute("user",user);
    }catch(AuthenticationException e){
        throw e;
    }
    return authenticationInfo;
}
```



## **授权流程**

![](img\e0aebca42fe84a25bd24e467f86fc2fd.png)

MyAuthorizingRealm中,关于权限赋予的代码

``` java
protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    log.info("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    //如果身份认证的时候没有传入User对象，这里只能取到userName
    //也就是SimpleAuthenticationInfo构造的时候第一个参数传递需要User对象
    User user  = (User)principals.getPrimaryPrincipal();

    for(SysRole role:user.getRoleList()){
        authorizationInfo.addRole(role.getRole());
        for(SysPermission p:role.getPermissions()){
            authorizationInfo.addStringPermission(p.getPermission());
        }
    }
    return authorizationInfo;
}
```

## 权限管理解决方案

###