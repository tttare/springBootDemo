package com.tttare.rabbit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: RabbitConfig <br/>
 * Description: <br/>
 * date: 2019/9/27 21:51<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Configuration
@EnableRabbit
@Slf4j
public class RabbitConfig {

    //******  rabbitMQ安装博客:https://www.cnblogs.com/sam-uncle/p/9050242.html
    //******  安装rabbitMQ,需先安装Erlang并配置环境变量
    //

    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.username}")
    private String userName;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.publisher-confirms}")
    private boolean isConfirm;
    @Value("${spring.rabbitmq.virtual-host}")
    private String vhost;
    @Value("${spring.rabbitmq.listener.simple.concurrency}")
    private int minConsumers;
    @Value("${spring.rabbitmq.listener.simple.max-concurrency}")
    private int maxConsumers;
    @Value("${spring.rabbitmq.listener.simple.acknowledge-mode}")
    private String acknowledgeMode;

    // RabbitMQ连接配置
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setVirtualHost(vhost);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setPublisherConfirms(isConfirm);
        return connectionFactory;
    }

    @Bean
    //boot 的amqp
    //******SpringAMQP项目对RabbitMQ做了很好的封装，可以很方便的手动声明队列，交换器，绑定
    /*AMQP(高级消息队列协议)，即Advanced Message Queuing Protocol，一个提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，
    为面向消息的中间件设计。基于此协议的客户端与消息中间件可传递消息，并不受客户端/中间件不同产品，不同的开发语言等条件的限制。
    Erlang中的实现有RabbitMQ等。*/
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    // 消息序列化方式(json字符串方式)
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //配置消费者监听的容器
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置确认模式手工确认
        return factory;
    }
}
