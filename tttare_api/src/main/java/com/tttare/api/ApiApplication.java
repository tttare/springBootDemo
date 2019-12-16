package com.tttare.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author tttare
 */
@SpringBootApplication
@EnableEurekaClient
//如果你是把你的工程拆分成聚合工程时，你的basePackages 属性一定要是你的Feign接口所在的包才行
@EnableFeignClients(basePackages= {"com.tttare.rabbitmqOps.user"}) // 要使用Feign，需要加上此注解
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
