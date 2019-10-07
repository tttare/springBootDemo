package com.tttare.rabbit.rabbitmqOps.publisher;

import com.tttare.rabbit.model.Mail;
import com.tttare.rabbit.model.MailLogger;
import com.tttare.rabbit.rabbitmqOps.repository.MailLoggerRepository;
import com.tttare.rabbit.rabbitmqOps.repository.MailRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class SteadyPublisher implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private MailLoggerRepository mailLoggerRepository;

    public void sendCallback(String routingKey, Mail mail, CorrelationData correlationData) {
        //备份消息 status为 0
        mail.setStatus("0");
        mailRepository.save(mail);
        rabbitTemplate.setConfirmCallback(this);

        log.info("CallBackSender  UUID: " + correlationData.getId());

        this.rabbitTemplate.convertAndSend(routingKey , mail , correlationData);
    }

    // 监控消息是否到达exchange
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("CallBackConfirm UUID: " + correlationData.getId());

        if(!ack) {
            //TODO
            //消息发送失败
            //根据 mailId 将status设置为 2
        }

        if(cause!=null) {
            System.out.println("CallBackConfirm Cause: " + cause);
            //TODO 加入一张log表 将correlationData.getId() 及 cause 存入 log表中
            mailLoggerRepository.save(new MailLogger(correlationData.getId(),cause));
        }
    }


}
