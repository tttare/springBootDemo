package com.tttare.rabbit.rabbitmqOps.publisher;

import com.tttare.rabbit.model.Mail;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName: PublisherWithCallBack <br/>
 * Description: <br/>
 * date: 2019/9/30 23:36<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Component
public class PublisherWithCallBack implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendCallback(String routingKey, Mail mail, CorrelationData correlationData) {
        rabbitTemplate.setConfirmCallback(this);

        System.out.println("CallBackSender  UUID: " + correlationData.getId());

        this.rabbitTemplate.convertAndSend(routingKey , mail , correlationData);
    }

    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("CallBackConfirm UUID: " + correlationData.getId());

        if(ack) {
            System.out.println("CallBackConfirm 消息消费成功！");
        }else {
            System.out.println("CallBackConfirm 消息消费失败！");
        }

        if(cause!=null) {
            System.out.println("CallBackConfirm Cause: " + cause);
        }
    }

}
