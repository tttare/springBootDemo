package com.tttare.rabbit.rabbitmqOps.controller;

import com.tttare.rabbit.model.Mail;
import com.tttare.rabbit.model.RabbitMQConstant;
import com.tttare.springDemo.model.ResponseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * ClassName: rabbitmqOpsController <br/>
 * Description: 对外提供rabbitMQ操作的接口<br/>
 * date: 2019/9/28 22:05<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Slf4j
@Controller
public class rabbitmqOpsController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //订阅模型-Fanout
    @RequestMapping("/pushFanoutMail")
    @ResponseBody
    public ResponseParam pushFanoutMail(@RequestBody Mail mail){
        ResponseParam rp;
        try{
            //发送消息
            rabbitTemplate.setExchange(RabbitMQConstant.FANOUT_EXCHANGE);
            rabbitTemplate.convertAndSend(mail);
            rp = new ResponseParam("000000","success");
        }catch (Exception e){
            log.error(e.getMessage());
            rp = new ResponseParam("000001","fail");
        }
        return rp;
    }




    //Direct   --- 做rabbitMQ的发送确认
    @RequestMapping("/pushDirectMail")
    @ResponseBody
    public ResponseParam pushDirectMail(@RequestBody Mail mail){
        ResponseParam rp;
        try{
            //发送消息
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("direct","orange",mail,correlationData);
            rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
                @Override
                public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                    if (ack) {
                        // 处理ack
                        System.out.println("CallBackConfirm 消息消费成功！");
                    } else {
                        // 处理nack, 此时cause包含nack的原因。

                        // 如当发送消息给一个不存在的Exchange。这种情况Broker会关闭Channel；

                        // 当Broker关闭或发生网络故障时，需要重新发送消息。

                        // 暂时先日志记录，包括correlationData, cause等。
                        System.out.println("CallBackConfirm 消息消费失败！");
                    }
                }
            });

            rp = new ResponseParam("000000","success");
        }catch (Exception e){
            log.error(e.getMessage());
            rp = new ResponseParam("000001","fail");
        }
        return rp;
    }


}
