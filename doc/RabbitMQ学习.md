#  RabbitMQ学习

## 1.rabbitMQ的消息模式

rabbitMQ的消息生产者和消费者之间的通信,不仅仅是只有一个队列,而是将发布者的消息通过Exchange交换器,将消息推送不同队列;

a.**Direct exchange**，一个exchange和多个queue绑定，会根据绑定的不同routingKey，发送到不同的Queue中

![](D:\学习笔记\img\directExchange.gif)

![](D:\学习笔记\img\2019-09-28_000904.gif)

b**.Topic exchange**，按模式匹配路由键。模式符号 "#" 表示一个或多个单词，"*" 仅匹配一个单词。

![](D:\学习笔记\img\2019-09-27_232441.gif)

c.**Fanout Exchange** , 不处理路由键。你只需要简单的将队列绑定到交换机上。一个发送到交换机的消息都会被转发到与该交换机绑定的所有队列上。很像子网广播，每台子网内的主机都获得了一份复制的消息。Fanout交换机转发消息是最快的。 

![](D:\学习笔记\img\2019-09-28_000949.gif)

d.**RPC**（Remote Procedure Call）远程过程调用方式。实际业务中，有的时候我们还需要等待消费者返回结果给我们，或者是说我们需要消费者上的一个功能、一个方法或是一个接口返回给我们相应的值。

![](D:\学习笔记\img\2019-09-27_232742.gif)

## 2.rabbitMQ的参数解释

**binding key**:在绑定（Binding）Exchange与Queue的同时，一般会指定一个binding key。在绑定多个Queue到同一个Exchange的时候，这些Binding允许使用相同的binding key.

**routing key**:生产者在将消息发送给Exchange的时候，一般会指定一个routing key，来指定这个消息的路由规则，生产者就可以在发送消息给Exchange时，通过指定routing key来决定消息流向哪里.

**Exchange Type**:RabbitMQ常用的Exchange Type有三种：fanout、direct、topic.

​      fanout:把所有发送到该Exchange的消息投递到所有与它绑定的队列中。

​      direct:把消息投递到那些binding key与routing key完全匹配的队列中。

​      topic:将消息路由到binding key与routing key模式匹配的队列中。

## 3.rabbitMQ结构图

![](D:\学习笔记\img\2019-09-27_235918.gif)

## 4.rabbitMQ安装及运行

rabbitmq由erlang语言编写,需要运行在erlang环境下,故需下载erlang,安装并配置环境变量

注意一定要注意rabbitmq与erlang的版本依赖关系,两个版本下载对了就不会有问题,版本关系对应如下:

https://www.rabbitmq.com/which-erlang.html#erlang-repositories

cmd窗口,erl 查看erlang的查看erlang是否安装配置成功,rabbitmqctl status查看rabbitMQ

(注:如果cmd erl和rabbitmqctl status都显示不是内部指令,这说明服务并未启动,要自己去bin(sbin)目录下启动服务,erlang是erl.exe,rabbitMQ是rabbitmq-server.bat)

输入rabbitmq-plugins enable rabbitmq_management 打开RabbitMQ的管理后台

在浏览器中访问地址 [http://localhost:15672/](https://links.jianshu.com/go?to=http%3A%2F%2Flocalhost%3A15672%2F)，就可以打开RabbitMQ的管理后台(初始密码guest/guest)。

**新建virtual host**(在RabbitMQ中可以虚拟消息服务器VirtualHost，每个VirtualHost相当月一个相对独立的RabbitMQ服务器，每个VirtualHost之间是相互隔离的。exchange、queue、message不能互通。 
在RabbitMQ中无法通过AMQP创建VirtualHost，可以通过以下命令来创建。)

![](D:\学习笔记\img\2019-09-28_191401.gif)

## 5.rabbitMQ的常用指令

- 查看用户列表：`rabbitmqctl list_users`
- 创建用户：`rabbitmqctl add_user {用户名} {密码}`
- 设置角色：`rabbitmqctl set_user_tags {用户名} {角色}`
- 设置权限：`rabbitmqctl set_permissions -p {虚拟主机} {用户名} {配置权限} {写权限} {读权限}`
- 删除用户：`rabbitmqctl delete_user {用户名}`

## 6.rabbitMQ整合到springboot

SpringAMQP项目对RabbitMQ做了很好的封装，可以很方便的手动声明队列，交换器，绑定;

AMQP(高级消息队列协议)，即Advanced Message Queuing Protocol，一个提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。基于此协议的客户端与消息中间件可传递消息，并不受客户端/中间件不同产品，不同的开发语言等条件的限制。Erlang中的实现有RabbitMQ等。

```xml
<!-- rabbitmq -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

```properties
# rabbitMQ连接的配置
spring:
  application:
    name: tttare-rabbit
  # rabbitMQ config
  rabbitmq:
    host: http://localhost:15672
    port: 5672
    username: admin
    password: tttare
    #publisher-confirms:当你的消息被RabbitMQ成功接收以后，提供了一个回调支持
    publisher-confirms: true
    #在RabbitMQ中可以虚拟消息服务器VirtualHost，每个VirtualHost相当月一个相对独立的RabbitMQ服务器，	   #每个VirtualHost之间是相互隔离的。exchange、queue、message不能互通。
    virtual-host:/tttare-queue
    listener:
      simple:
        acknowledge-mode: manual #设置确认模式手工确认
        #参考博客:https://blog.csdn.net/weixin_38380858/article/details/84963944
        #AcknowledgeMode.NONE:rabbitmq server默认推送的所有消息都已经消费成功，会不断地向消费端推  		 #送消息,因为rabbitmq server认为推送的消息已被成功消费，所以推送出去的消息不会暂存在server端
        #AcknowledgeMode.AUTO:由spring-rabbit依据消息处理逻辑是否抛出异常自动发送ack（无异常）或  		 #nack（异常）到server端
        #AcknowledgeMode.MANUAL:需要人为地获取到channel之后调用方法向server发送ack（或消费失败时的 		  #nack）信息
        #  **************************
        #无ack模式：效率高，存在丢失大量消息的风险。
		#有ack模式：效率低，不会丢消息。
		#  *********************************
		#注意，有ack的模式下，需要考虑setDefaultRequeueRejected(false)，否则当消费消息抛出异常没有		   #catch住时，这条消息会被rabbitmq放回到queue头部，再被推送过来，然后再抛异常再放回…死循环了。设         #置false的作用是抛异常时不放回，而是直接丢弃，所以可能需要对这条消息做处理，以免丢失。更详细的配         #置参考这里。
        concurrency: 3 #消费者最小数量
        max-concurrency: 10 #消费者最大数量
```

## 7.rabbitMQ的消息类型

### 1.基本消息类型

RabbitMQ是一个消息代理：它接受和转发消息。 你可以把它想象成一个邮局：当你把邮件放在邮箱里时，你可以确定邮差先生最终会把邮件发送给你的收件人。 在这个比喻中，RabbitMQ是邮政信箱，邮局和邮递员。
RabbitMQ与邮局的主要区别是它不处理纸张，而是接受，存储和转发数据消息的二进制数据块

![](D:\学习笔记\img\2019-09-29_174527.gif)

P（producer/ publisher）：生产者，一个发送消息的用户应用程序。

C（consumer）：消费者，消费和接收有类似的意思，消费者是一个主要用来等待接收消息的用户应用程序

队列（红色区域）：rabbitmq内部类似于邮箱的一个概念。虽然消息流经rabbitmq和你的应用程序，但是它们只能存储在队列中。队列只受主机的内存和磁盘限制，实质上是一个大的消息缓冲区。许多生产者可以发送消息到一个队列，许多消费者可以尝试从一个队列接收数据。

总之：生产者将消息发送到队列，消费者从队列中获取消息，队列是存储消息的缓冲区

### 2.work消息模型

工作队列或者竞争消费者模式

![](D:\学习笔记\img\2019-09-29_174802.gif)

工作队列，又称任务队列。主要思想就是避免执行资源密集型任务时，必须等待它执行完成。相反我们稍后完成任务，我们将任务封装为消息并将其发送到队列。 在后台运行的工作进程将获取任务并最终执行作业。当你运行许多工人时，任务将在他们之间共享，但是一个消息只能被一个消费者获取。
总之：让多个消费者绑定到一个队列，共同消费队列中的消息。队列中的消息一旦消
费，就会消失，因此任务是不会被重复执行的

### 3.订阅模型（三类）

![](D:\学习笔记\img\2019-09-29_175112.gif)

解读:

1、1个生产者，多个消费者

2、每一个消费者都有自己的一个队列

3、生产者没有将消息直接发送到队列，而是发送到了交换机

4、每个队列都要绑定到交换机

5、生产者发送的消息，经过交换机到达队列，实现一个消息被多个消费者获取的目的

X（Exchanges）：交换机一方面：接收生产者发送的消息。另一方面：知道如何处理消息，例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作，取决于Exchange的类型。

**Exchange类型有以下几种**：

```xml
 Fanout：广播，将消息交给所有绑定到交换机的队列
 
 Direct：定向，把消息交给符合指定routing key 的队列

 Topic：通配符，把消息交给符合routing pattern（路由模式） 的队列
```
Exchange（交换机）只负责转发消息，不具备存储消息的能力，**因此如果没有任何队列与Exchange绑定，或者没有符合路由规则的队列，那么消息会丢失**！

#### （1）订阅模型-Fanout

**Fanout**，也称为广播。
在广播模式下，消息发送流程是这样的：

- 1） 可以有多个消费者
- 2） 每个消费者有自己的queue（队列）
- 3） 每个队列都要绑定到Exchange（交换机）
- 4） 生产者发送的消息，只能发送到交换机，交换机来决定要发给哪个队列，生产者无法决定。
- 5） 交换机把消息发送给绑定过的所有队列
- 6） 队列的消费者都能拿到消息。实现一条消息被多个消费者消费

#### （2）订阅模型-Direct:

![](D:\学习笔记\img\2019-09-29_175709.gif)

在Direct模型下，队列与交换机的绑定，不能是任意绑定了，而是要指定一个RoutingKey（路由key），消息的发送方在向Exchange发送消息时，也必须指定消息的routing key。

P：生产者，向Exchange发送消息，发送消息时，会指定一个routing key。

X：Exchange（交换机），接收生产者的消息，然后把消息递交给 与routing key完全匹配的队列

C1：消费者，其所在队列指定了需要routing key 为 error 的消息

C2：消费者，其所在队列指定了需要routing key 为 info、error、warning 的消息

#### （3）订阅模型-Topic

![](D:\学习笔记\img\2019-09-29_175829.gif)

Topic 类型的 Exchange 与 Direct 相比，都是可以根据 RoutingKey 把消息路由到不同的队列。只不过 Topic 类型 Exchange 可以让队列在绑定 Routing key 的时候使用通配符！

通配符规则：#：匹配一个或多个词*：匹配不多不少恰好 1 个词

## 8.rabbitMQ实战

### 前言

上面的几种消息类型中,出现了**基本消息类型**和**work消息模型**都是生产者与消费者直接通过queue连接通信,这并非rabbitMQ推荐的写法;

```java
org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;//这是amqp的消息发送核心类
```

![1569829720225](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1569829720225.png)

如上代码可知,虽然可以rabbitTemplate.setQueue(queue)将消息直接发送给队列,但是已经被标记为***废弃***

**故,我们使用rabbitMQ推送消息时,生产者与队列之间要经过Exchange(交换器),由交换器决定将消息发送给那个队列**

### 订阅模型-Fanout(广播)

上文已详细解释了Fanout,简言之,就是交换机把消息Exchange发送给绑定过的所有队列

```java
org.springframework.amqp.core.FanoutExchange;//广播类型的交换器
//建立ExchangeConfig,在这个类中定义交换器
@Bean
@Qualifier(RabbitMQConstant.FANOUT_EXCHANGE)
// 定义一个FanoutExchange
public FanoutExchange fanoutExchange(){
    return new FanoutExchange(RabbitMQConstant.FANOUT_EXCHANGE,false,true);
}

//在这个类中定义队列两个队列
@Bean
@Qualifier(RabbitMQConstant.FANOUT_QUEUE_ONE)
public Queue queue01(){
        /**
         * 参数解析:
         * param1:队列的名称
         * param2:是否持久化, 队列的声明默认是存放到内存中的，如果rabbitmq重启会丢失，如果想重启之后还存在就要使队列持久化，保存到Erlang自带的Mnesia数据库中，当rabbitmq重启之后会读取该数据库
         * param3:是否排外的，有两个作用，一：当连接关闭时connection.close()该队列是否会自动删除；二：该队列是否是私有的private，如果不是排外的，可以使用两个消费者都访问同一个队列，没有任何问题，如果是排外的，会对当前队列加锁，其他通道channel是不能访问的，如果强制访问会报异常：com.rabbitmq.client.ShutdownSignalException: channel error; protocol method: #method<channel.close>(reply-code=405, reply-text=RESOURCE_LOCKED - cannot obtain exclusive access to locked queue 'queue_name' in vhost '/', class-id=50, method-id=20)一般等于true的话用于一个队列只能有一个消费者来消费的场景
         * param4:是否自动删除，当最后一个消费者断开连接之后队列是否自动被删除，可以通过RabbitMQ Management，查看某个队列的消费者数量，当consumers = 0时队列就会自动删除
         * */
    return new Queue(RabbitMQConstant.FANOUT_QUEUE_ONE, false, false, true);
}

@Bean
@Qualifier(RabbitMQConstant.FANOUT_QUEUE_TWO)
public Queue queue02(){
    return new Queue(RabbitMQConstant.FANOUT_QUEUE_TWO, false, false, true);
}
//一个队列可以绑定多个交换器,一个交换器可以绑定多个队列,完全是一个多对多关系
//定义队列与交换器的绑定关系
@Bean
@Qualifier(RabbitMQConstant.FANOUT_BINDING)
public Binding binding() {
    return BindingBuilder.bind(queue02()).to(fanoutExchange());
}
```

以上的代码,已经定义了两个队列,一个FanoutExchange,并将两个队列与交换器绑定,下面来编写生产者

``` java
//订阅模型-Fanout
@RequestMapping("/pushFanoutMail")
@ResponseBody
public ResponseParam pushFanoutMail(@RequestBody Mail mail){
    ResponseParam rp;
    try{
        //生产者绑定的交换器
        rabbitTemplate.setExchange(RabbitMQConstant.FANOUT_EXCHANGE);
        //发送消息
        rabbitTemplate.convertAndSend(mail);
        rp = new ResponseParam("000000","success");
    }catch (Exception e){
        log.error(e.getMessage());
        rp = new ResponseParam("000001","fail");
    }
    return rp;
}
```

消费者代码编写

```java
@SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = RabbitMQConstant.FANOUT_EXCHANGE, type = ExchangeTypes.FANOUT,durable = RabbitMQConstant.FALSE_CONSTANT, autoDelete = RabbitMQConstant.TRUE_CONSTANT),
            value = @Queue(value = RabbitMQConstant.FANOUT_QUEUE_ONE, durable = RabbitMQConstant.FALSE_CONSTANT,
                    autoDelete = RabbitMQConstant.TRUE_CONSTANT)
            /*FANOUT 不需要key ,key = RabbitMQConstant.CONFIRM_KEY*/),
            containerFactory = "rabbitListenerContainerFactory")
public void process(Mail mail, Channel channel,@Header(name = "amqp_deliveryTag") long deliveryTag,@Header("amqp_redelivered") boolean redelivered, @Headers Map<String, String> head) {
        try {
            channel.basicAck(deliveryTag,true);
            //channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            log.info("------receive: " + mail.toString());
            log.info("------header:"+head);
            channel.close();
        } catch (Exception e) {
            log.error("consume confirm error!", e);
            //这一步千万不要忘记，不会会导致消息未确认，消息到达连接的qos之后便不能再接收新消息
            //一般重试肯定的有次数，这里简单的根据是否已经重发过来来决定重发。第二个参数表示是否重新分发
            channel.basicReject(deliveryTag, !redelivered);
            //这个方法我知道的是比上面多一个批量确认的参数
            // channel.basicNack(deliveryTag, false,!redelivered);
        }
    }


@SneakyThrows
    @RabbitListener(queues=RabbitMQConstant.FANOUT_QUEUE_TWO,containerFactory = "rabbitListenerContainerFactory")
public void process2(Mail mail, Channel channel,@Header(name = "amqp_deliveryTag") long deliveryTag,@Header("amqp_redelivered") boolean redelivered, @Headers Map<String, String> head) {
        try {
            channel.basicAck(deliveryTag,true);
            log.info("------receive,来自消费者二: " + mail.toString());
            log.info("------header:"+head);
            channel.close();
        } catch (Exception e) {
            log.error("consume confirm error!", e);
            channel.basicReject(deliveryTag, !redelivered);
        }
    }
```

消费者,使用的注解比较多,我们来一点点的了解一下

 @RabbitListener:注解指定目标方法来作为消费消息的方法，通过注解参数指定所监听的队列或者Binding。使用      	@RabbitListener可以设置一个自己明确默认值的`RabbitListenerContainerFactory`对象。

注解参数:就是声明一组交换器与队列的绑定,exchange:定义交换器,value:绑定的队列

注解上的binding实际就声明了交换器与队列的绑定bindings

上面的代码 我工提供了两种绑定和消费方式

#### 队列交换器绑定与消费方式一:

绑定:

```java
//在配置类中绑定
@Bean
@Qualifier(RabbitMQConstant.FANOUT_BINDING)
public Binding binding() {
    //交换器与队列(fanoutQueueTWO)绑定上了(没有绑定fanoutQueueOne)
    return BindingBuilder.bind(queue02()).to(fanoutExchange());
}
```

消费:

```java
@SneakyThrows
    //绑定fanoutQueueTWO这个队列,故直接消费次队列的消息就行,@RabbitListener只需要只需要指向		//fanoutQueueTWO就行
    @RabbitListener(queues=RabbitMQConstant.FANOUT_QUEUE_TWO,
            containerFactory = "rabbitListenerContainerFactory")
    public void process2(Mail mail, Channel channel,@Header(name = "amqp_deliveryTag") long deliveryTag,@Header("amqp_redelivered") boolean redelivered, @Headers Map<String, String> head) {
        try {
            channel.basicAck(deliveryTag,true);
            log.info("------receive,来自消费者二: " + mail.toString());
            log.info("------header:"+head);
            channel.close();
        } catch (Exception e) {
            log.error("consume confirm error!", e);
            channel.basicReject(deliveryTag, !redelivered);
        }
    }
```

#### 队列交换器绑定与消费方式二:

在消费者的注解上,这样去声明交换器与队列的绑定

```java
 @RabbitListener(bindings = @QueueBinding(exchange = @Exchange(value = RabbitMQConstant.FANOUT_EXCHANGE, type = ExchangeTypes.FANOUT,durable = RabbitMQConstant.FALSE_CONSTANT, autoDelete = RabbitMQConstant.TRUE_CONSTANT),value = @Queue(value = RabbitMQConstant.FANOUT_QUEUE_ONE, durable = RabbitMQConstant.FALSE_CONSTANT,
                    autoDelete = RabbitMQConstant.TRUE_CONSTANT)
            /*FANOUT 不需要key ,key = RabbitMQConstant.CONFIRM_KEY*/),
            containerFactory = "rabbitListenerContainerFactory")
```

两种绑定,都能将交换器与队列绑定起来,如下管理后台截图图

![](D:\学习笔记\img\2019-09-30_213410.gif)

**注:由于我想尝试两种绑定方式,也出了一个bug**

```java
//我在配置类中new了个FanoutExchange
new FanoutExchange(RabbitMQConstant.FANOUT_EXCHANGE);
//又在注解上定义了
exchange = @Exchange(value = RabbitMQConstant.FANOUT_EXCHANGE, type = ExchangeTypes.FANOUT,durable = RabbitMQConstant.FALSE_CONSTANT, autoDelete = RabbitMQConstant.TRUE_CONSTANT)
//这里我的错误是,在两处定义两个相同的名称的交换器,但是,构造参数确不同;定义一个对象时,构造参数只用了一个参数,第二个确用了三个构造参数,直接导致项目启动失败
new FanoutExchange(RabbitMQConstant.FANOUT_EXCHANGE,false,true);
//改成这样后,删除这个交换器,重启项目,就OK了
```

rabbitMQ很多启动失败的原因是,**同一个对象,在两处,定义参数不同,**我建议,这种队列交换器绑定与消费方式还是使用**第一种**,这样就也简单不会出错

```java
在配置类上new FanoutExchange(...);new Queue(...);BindingBuilder.bind(.).to(.)
与在@RabbitListener(bindings=...)的操作,本质上,都是一样的;
哪种更好,看个人喜好
```

#### 结果展示:

两个与交换器绑定的队列都收到消息,且都消费成功

![](D:\学习笔记\img\2019-09-30_215207.gif)

### 订阅模型-Direct(直连)





### 订阅模式-Topic



## 9.消息发送和消息消费健壮性处理

