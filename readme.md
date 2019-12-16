# SpringDemo项目结构说明

## doc

学习笔记:es学习笔记,rabbitMQ学习笔记,springCloud学习笔记

## tttare_api

FeignClient公用接口层,对外提供微服务接口

port: 8081

## tttare_portal

用户访问入口工程,主要是处理视图,和资源的请求

port: 8083

## tttare_core

controller继承tttare_api的FeignClient接口,主要提供数据库的访问和业务逻辑

port: 8082

## tttare_model

公用model类及工具类

## tttare_elasticsearch

elasticsearch文档搜索工程

port:8087

## tttare_rabbit

rabbitMQ消息中间件层

port:8089

## tttare_eurerkaServer

eurerka服务注册及发现server

http://localhost:8761/eureka/

## tttare_management

后台管理工程

port:8088