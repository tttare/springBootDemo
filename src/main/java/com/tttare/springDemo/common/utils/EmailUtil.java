package com.tttare.springDemo.common.utils;


import com.tttare.springDemo.common.model.Contant;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * ClassName: EmailUtil <br/>
 * Description: 邮件发送工具类<br/>
 * date: 2019/9/6 15:33<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public class EmailUtil {
    /**
     * 邮件发送的方法
     *
     * @param email 收件人邮箱
     * @param subject 主题
     * @param content 内容
     * @return 成功或失败
     */
    public static boolean send(String email, String subject, String content) {

        // 第一步：创建Session
        Properties props = new Properties();
        // 指定邮件的传输协议，smtp(Simple Mail Transfer Protocol 简单的邮件传输协议)
        props.put("mail.transport.protocol", "smtp");
        // 指定邮件发送服务器服务器 "smtp.qq.com"
        props.put("mail.host", "smtp.163.com");
        // 指定邮件的发送人(您用来发送邮件的服务器，比如您的163\sina等邮箱)
        props.put("mail.from", Contant.DEFAULT_EMAIL);
        /*if (true) {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.socketFactory.port", sendPort);
        }*/
        Session session = Session.getDefaultInstance(props);

        // 开启调试模式
        //session.setDebug(true);
        try {
            // 第二步：获取邮件发送对象
            Transport transport = session.getTransport();
            // 连接邮件服务器，链接您的163、sina邮箱，用户名（不带@163.com，登录邮箱的邮箱账号，不是邮箱地址）、密码
            transport.connect(Contant.DEFAULT_EMAIL_NAME, Contant.DEFAULT_MMAIL_PASSWORD);
            Address toAddress = new  InternetAddress(email);

            // 第三步：创建邮件消息体
            MimeMessage message = new MimeMessage(session);
            //设置在发送给收信人之前给自己（发送方）抄送一份，不然会被当成垃圾邮件，报554错
            message.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(Contant.DEFAULT_EMAIL));
            //设置自定义发件人昵称
            String nick="";
            try {
                nick=javax.mail.internet.MimeUtility.encodeText(Contant.WEBSITE_NAME);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message.setFrom(new InternetAddress(nick+" <"+Contant.DEFAULT_EMAIL+">"));
            // 邮件的主题
            message.setSubject(subject);
            //收件人
            message.addRecipient(Message.RecipientType.TO, toAddress);
            /*//抄送人
            Address ccAddress = new InternetAddress("first.lady@whitehouse.gov");
            message.addRecipient(Message.RecipientType.CC, ccAddress);*/
            // 邮件的内容
            message.setContent(content, "text/html;charset=utf-8");
            // 邮件发送时间
            message.setSentDate(new Date());

            // 第四步：发送邮件
            // 第一个参数：邮件的消息体
            // 第二个参数：邮件的接收人，多个接收人用逗号隔开（test1@163.com,test2@sina.com）
            transport.sendMessage(message, InternetAddress.parse(email));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.print(send("1114647302@qq.com","邮件发送测试","test123456"));
    }
}
