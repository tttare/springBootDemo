package com.tttare.eurekaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * ClassName: EurekaServer <br/>
 * Description: <br/>
 * date: 2019/9/9 9:47<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    public static void main (String[] args){
        SpringApplication.run(EurekaApplication.class,args);
    }
}
