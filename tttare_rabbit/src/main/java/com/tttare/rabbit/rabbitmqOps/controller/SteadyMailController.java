package com.tttare.rabbit.rabbitmqOps.controller;

import com.tttare.rabbit.model.Mail;
import com.tttare.rabbit.rabbitmqOps.publisher.SteadyPublisher;
import com.tttare.springDemo.model.ResponseParam;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: SteadyMailController <br/>
 * Description: <br/>
 * date: 2019/10/7 21:23<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Controller("/steady")
public class SteadyMailController extends SteadyPublisher {

    @RequestMapping("/pushDirectMail")
    @ResponseBody
    public ResponseParam pushDirectMail(@RequestBody Mail mail){
        CorrelationData correlationData = new CorrelationData(mail.getMailId());
        /*param1:交换器
         * param2:routingKey
         * param3:消息
         * param4:correlationData是生产者在发送数据时可以携带的相关信息，比如消息唯一属性,uuid*/
        try{
            sendCallback("orange",mail,correlationData);
            return new ResponseParam("000000","success");
        }catch (Exception e){
            return new ResponseParam("000001",e.getMessage(),"fail");
        }
    }
}
