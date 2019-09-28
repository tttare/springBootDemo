package com.tttare.rabbit.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: ProducerConsumerConfig <br/>
 * Description: <br/>
 * date: 2019/9/27 22:33<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Configuration
//生产者消费者模式的配置,包括一个队列和两个对应的消费者
public class ProducerConsumerConfig {
    @Bean
    public Queue myQueue() {
        Queue queue = new Queue("myqueue");
        return queue;
    }

}
