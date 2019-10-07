package com.tttare.rabbit.rabbitmqOps.repository;

import com.tttare.rabbit.model.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ClassName: MailRepository <br/>
 * Description: <br/>
 * date: 2019/10/7 21:37<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public interface MailRepository extends JpaRepository<Mail, String> {

}
