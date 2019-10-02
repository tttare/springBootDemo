package com.tttare.rabbit.config;

import com.tttare.rabbit.model.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: FanoutConfig <br/>
 * Description: <br/>
 * date: 2019/9/30 18:19<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Configuration
//Fanout(广播):就是交换机把消息Exchange发送给绑定过的所有队列
public class FanoutConfig {

    //定义一个交换器
    @Bean
    @Qualifier(RabbitMQConstant.FANOUT_EXCHANGE)
    // 定义一个FanoutExchange
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(RabbitMQConstant.FANOUT_EXCHANGE,false,true);
    }

    //定义两个队列
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


    //定义一个连接
    @Bean
    @Qualifier(RabbitMQConstant.FANOUT_BINDING)
    public Binding binding() {
        return BindingBuilder.bind(queue02()).to(fanoutExchange());
    }
}
