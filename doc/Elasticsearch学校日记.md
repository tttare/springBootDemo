# Elasticsearch学校日记

## 简介

Elasticsearch(下文简称es) 是一个分布式的 RESTful 风格的搜索和数据分析引擎。

- 查询 ： Elasticsearch 允许执行和合并多种类型的搜索 — 结构化、非结构化、地理位置、度量指标 — 搜索方式随心而变。
- 分析 ： 找到与查询最匹配的十个文档是一回事。但是如果面对的是十亿行日志，又该如何解读呢？Elasticsearch 聚合让您能够从大处着眼，探索数据的趋势和模式。
- 速度 ： Elasticsearch 很快。真的，真的很快。
- 可扩展性 ： 可以在笔记本电脑上运行。 也可以在承载了 PB 级数据的成百上千台服务器上运行。
- 弹性 ： Elasticsearch 运行在一个分布式的环境中，从设计之初就考虑到了这一点。
- 灵活性 ： 具备多个案例场景。数字、文本、地理位置、结构化、非结构化。所有的数据类型都欢迎。
- HADOOP & SPARK ： Elasticsearch + Hadoop

## es的安装

es版本与jdk版本对应

![](D:\学习笔记\img\20180920164546532.png)

es下载地址:https://www.elastic.co/guide/en/elasticsearch/reference/index.html

es 5.5版本下载,需要jdk1.8以:上https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.5.3.msi

安装后查看es状态:http://localhost:9200/

![](D:\学习笔记\img\2019-10-01_175014.gif)

