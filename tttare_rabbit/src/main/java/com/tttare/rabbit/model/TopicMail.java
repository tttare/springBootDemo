package com.tttare.rabbit.model;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName: TopicMail <br/>
 * Description: <br/>
 * date: 2019/9/28 21:56<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Getter
@Setter
public class TopicMail extends Mail {
    String routingkey;
}
