# SpringCloud学习日志

##  Day One 服务的注册与发现

> SpringCloud核心子项目
>
> - Spring Cloud Netflix：核心组件，可以对多个Netflix OSS开源套件进行整合，包括以下几个组件：
>   - Eureka [juˈriːkə]：服务治理组件，包含服务注册与发现
>   - Hystrix ：容错管理组件，实现了熔断器
>   - Ribbon [ˈrɪbən]：客户端负载均衡的服务调用组件
>   - Feign [feɪn]：基于Ribbon和Hystrix的声明式服务调用组件
>   - Zuul：网关组件，提供智能路由、访问过滤等功能
>   - Archaius [ɑr'keɪk] ：外部化配置组件
> - Spring Cloud Config：配置管理工具，实现应用配置的外部化存储，支持客户端配置信息刷新、加密/解密配置内容等。
> - Spring Cloud Bus：事件、消息总线，用于传播集群中的状态变化或事件，以及触发后续的处理
> - Spring Cloud Security：基于spring security的安全工具包，为我们的应用程序添加安全控制
> - Spring Cloud Consul : 封装了Consul操作，Consul是一个服务发现与配置工具（与Eureka作用类似），与Docker容器可以无缝集成

###  1.创建集群EurekaServer,来实现微服务的注册与发现

* 导入EurekaServer及springCloud的依赖

  ~~~xml
  <!-- spring cloud dependencies -->
  <dependencyManagement>
      <dependencies>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-dependencies</artifactId>
              <version>Edgware.SR3</version>
              <type>pom</type>
              <scope>import</scope>
          </dependency>
      </dependencies>
  </dependencyManagement>
  <!-- eureka server -->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-eureka-server</artifactId>
  </dependency>
  ~~~

  

* 启动类加注解

  ~~~java
  @SpringBootApplication
  @EnableEurekaServer
  public class EurekaApplication {
  
      public static void main (String[] args){
          SpringApplication.run(EurekaApplication.class,args);
      }
  }
  ~~~

* 配置eureka server 集群,多节点配置文件

~~~yaml
# 配置多节点eureka server的原因:如果是单节点的server,如果服务宕机,则eureka client就无法新注册和发现
# 其他微服务.虽然eureka client有本地缓存,暂时可以不急,但是对于新加入的微服务及突然不可用的微务,eureka # client就不知道,缓存长期不能更新,影响微服务的调用,多节点,就是为防止这种情况

# yaml 被---分为三段
# spring profiles 定义了没一段的运行环境,可以用这个配置来区分开发 测试 生产环境

# 第一段 未定义profiles,故其为全局配置,在每个profile都生效
# 第二段 定义了一个peer1(hostname 本地的话 就是127.0.0.1),这个配置中 eureka.instance定义了一个
# peer1的eureka server实例,eureka.client.serviceUrl,将该服务注册到主机peer2的eureka server上去
# 第三段 同理 定义一个peer2,注册到peer1的eureka server上去
# 如下配置 定义了两个eureka server,启动服务的时候,就两个server互相注册,两个server的信息就能够同步
spring:
  application:
    name: tttare-eureka-service
---
spring:
  # profile=peer1
  profiles: peer1
server:
  port: 8761
eureka:
  instance:
    # when profile=peer1, hostname=peer1
    hostname: peer1
  client:
    service-url:
      # register self to peer2
      defaultZone: http://peer2:8762/eureka
---
spring:
  # profile=peer2
  profiles: peer2
server:
  port: 8762
eureka:
  instance:
    # when profile=peer2, hostname=peer2
    hostname: peer2
  client:
    service-url:
      # register self to peer1
      defaultZone: http://peer1:8761/eureka
~~~

* 启动多节点的eureka server

  项目用maven打jar包(启动报缺少主程序清单解决办法:https://www.jianshu.com/p/9068a3a42629)

~~~java
java -jar tttare_eurerkaServer-0.0.1-SNAPSHOT.jar --spring.boot.profiles.active=peer1
java -jar tttare_eurerkaServer-0.0.1-SNAPSHOT.jar --spring.boot.profiles.active=peer2
~~~

C:\Windows\System32\drivers\etc\hosts文件加上: 127.0.0.1  peer1  peer2

在cmd黑窗口  ping peer1时 显示ip为 127.0.0.1是,证明配置成功



## Day Two  使用Feign实现声明式REST调用 





## 使用Docker部署微服务

