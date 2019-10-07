package com.tttare.rabbit.rabbitmqOps.consumers;

import com.rabbitmq.client.Channel;
import com.tttare.rabbit.model.Mail;
import com.tttare.rabbit.model.RabbitMQConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ClassName: DirectConsumer <br/>
 * Description: <br/>
 * date: 2019/9/30 23:09<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Component
@Slf4j
public class DirectConsumer {

    @SneakyThrows
    @RabbitListener(queues = "directqueue1")
    public void process1(Mail mail, Channel channel, @Header(name = "amqp_deliveryTag") long deliveryTag,
                         @Header("amqp_redelivered") boolean redelivered, @Headers Map<String, String> head) {
        try {
            //根据mailId查询是否有已有被消费的mail,如有,不能重复消费
            //----新消息   处理业务逻辑-----

            Thread.sleep(1000);
            log.info("------receive,消费者一,队列的orange: " + mail.toString());
            log.info("------header:"+head);
            //  根据mailId将status改为 1 已消费
            //----已处理的消息  直接将消息ack------
            channel.basicAck(deliveryTag,true);
            channel.close();
        } catch (Exception e) {
            log.error("consume confirm error!", e);
            //这一步千万不要忘记，不会会导致消息未确认，消息到达连接的qos之后便不能再接收新消息
            //一般重试肯定的有次数，这里简单的根据是否已经重发过来来决定重发。第二个参数表示是否重新分发
            /**
             * deliveryTag:该消息的index
             * requeue：被拒绝的是否重新入队列  redelivered为true时,认为
             * */
            channel.basicReject(deliveryTag, !redelivered);
            //TODO
            //  根据mailId将status改为 3 消费失败

            //  可以将异常  存入logger库 标准为 消费时异常

            // 最后 设置定时任务  每晚 将是失败的mail 重新发送 状态为 2 3的消息
        }
    }

    @SneakyThrows
    @RabbitListener(queues = "directqueue1")
    public void process2(Mail mail, Channel channel, @Header(name = "amqp_deliveryTag") long deliveryTag,
                         @Header("amqp_redelivered") boolean redelivered, @Headers Map<String, String> head) {
        try {
            /**
             * deliveryTag:该消息的index
             * multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息。
             */
            channel.basicAck(deliveryTag,true);
            Thread.sleep(1000);
            log.info("------receive,消费者二,队列的orange: " + mail.toString());
            log.info("------header:"+head);
            channel.close();
        } catch (Exception e) {
            log.error("consume confirm error!", e);
            //是接收端告诉服务器这个消息我拒绝接收,不处理,可以设置是否放回到队列中还是丢掉，而且只能一次拒绝一个消息,官网中有明确说明不能批量拒绝消息，为解决批量拒绝消息才有了basicNack。
            /**
             * deliveryTag:该消息的index
             * requeue：被拒绝的是否重新入队列
             * */
            channel.basicReject(deliveryTag, !redelivered);
            //这个方法我知道的是比上面多一个批量确认的参数
            // channel.basicNack(deliveryTag, false,!redelivered);
        }
    }
}
