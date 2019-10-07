package com.tttare.rabbit.model;

/**
 * ClassName: MailLogger <br/>
 * Description: <br/>
 * date: 2019/10/8 0:07<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public class MailLogger {

    private String mailId;
    private String cause;

    public MailLogger(String mailId,String cause){
        this.mailId=mailId;
        this.cause=cause;
    }
}
