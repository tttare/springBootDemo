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

![](img\20180920164546532.png)

es下载地址:https://www.elastic.co/guide/en/elasticsearch/reference/index.html

es 5.5版本下载,需要jdk1.8以:上https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.5.3.msi

安装后查看es状态:http://localhost:9200/

![](img\2019-10-01_175014.gif)

## es管理后台和ik分词器安装

Ealsticsearch只是后端提供各种API，那么怎么直观的使用它呢？Elasticsearch-head将是一款专门针对于Elasticsearch的客户端工具

**Elasticsearch-head**配置包，下载地址：https://github.com/mobz/elasticsearch-head

**ik分词器要与es版本一致**,下载地址:https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v5.5.3

参考博客:https://blog.csdn.net/chen_2890/article/details/83757022

## es名称概念解析

**强推这篇文章,**下面的观点,也是基本取材这篇文章:http://developer.51cto.com/art/201904/594615.htm

### 倒排索引

倒排索引源于实际应用中需要根据属性的值来查找记录。这种索引表中的每一项都包括一个属性值和具有该属性值的各记录的地址。由于不是由记录来确定属性值，而是由属性值来确定记录的位置，因而称为倒排索引(inverted index)。(注:**上文博客,用古诗词的记忆,很通俗的描述了倒排索引的概念**)

### 索引(index),类型(type),文档(document)

![](img\2019-10-02_155927.gif)

**索引**:es吧数据放到一个或者多个索引中，如果用关系型数据库模型对比，索引的地位与数据库实例（db）相当;索引存放和读取的基本单元是文档 （document）。es内部使用的是apache lucene实现的索引中数据的读写。（es被视为单独的一个索引，在lucene中不止一个，因为分布式中，es会用到分区shards和备份 replicas机制讲一个索引存储多份）。

**文档**:在es中，文档主要是存储实体。所有的es应用需求最后都需要统一建成一个检索模型：检索相关文档。文档由一个或多个域，每个域field由一个域名或多个值组成（有多个值的称为多值域）。在es中每个文档都可能会有不同的域field集合；也就是说文档是没有固定的模式和同意的结构的。文档之间保持的相似性即可。在客户端角度来看，**文档就是一个json对象**。

**文档类型**（type）:每个文档在es中都必须设定它的类型。文档类型使得同一个索引中在存储结构不同文档时，只需根据文档类型就可以找到对应的参数映射信息，方便文档的存取。

**参数映射**:所有的文档在存储之前都必须分析（analyze）流程，用户可以配置输入文本分解成token的方式：哪些token被滤掉；或者其它的处理流程，比如去除html标签。

![](img\2019-10-02_160806.gif)

上图表现的很清晰:**索引**类似于mysql的数据库**,类型**类似于mysql的表结构定义,**文档**则是mysql中一行行的数据

### 分词

分词涉及到很多文档搜索的实际情况考虑,不仅仅是简单的拆词,过滤,还包括中英文同义词的处理

参考博客:https://www.jianshu.com/p/914f102bc174

type:text与keyword都是表示字符串,text要分词,keyword不分词

## es分布式原理



## es整合springboot

### 踩坑一

最开始使用的是spring-boot-starter-data-elasticsearch,项目启动正常

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

但是,对es的操作,都会报如下的异常,在确认es配置无误之后,只有可能是maven依赖的原因

**None of the configured nodes are available: [{#transport#-1}{OkLKCpyRQY-7k4rR8Q6h0A}{127.0.0.1}**

**es根据不同版本,对java连接工具要求都不同,真是一个大坑**

在确定启动es,并确定自己的es版本是**5.5.3**之后,我们选择spring data Elasticsearch,版本选择如下

![](img\2019-10-05_000506.gif)

新的依赖如下

``` xml
<!--一定要声明自己的es版本-->
<properties>
        <elasticsearch.version>5.5.3</elasticsearch.version>
</properties>
<!--spring-boot-starter-data-elasticsearch这个依赖支持的es版本太低了(es 2.0左右),高版本的es需要安上图的对应关系,选择spring-data-elasticsearch这个包的-->
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-elasticsearch</artifactId>
    <version>3.0.0.RELEASE</version>
</dependency>
```

**<elasticsearch.version>5.5.3</elasticsearch.version>,会让如下的elasticsearch的三个连接工具与你使用的es版本一致,重点就是java连接tool要与运行的es版本一致,都是5.5.3**

![](img\2019-10-05_134146.gif)

**根据自己的es版本,选择对应的maven依赖,这真是太重要了**

### 踩坑二

``` properties
spring:
  application:
    name: tttare-elasticsearch
  data:
    elasticsearch:
      cluster-name: elasticsearch
      cluster-nodes: 127.0.0.1:9300
```

很多人,nodes的端口配置的是9200,这肯定是连不上的,9200是http连接端口,**java开发要连9300**,tcp连接端口;

127.0.0.1:9200这个端口也很重要,后文将详细描述用这个端口查看集群及es的数据

### spring-data-elasticsearch代码编写

``` xaml
熟悉spring data系列的同学应该了解,spring data 通过继承特定Repository类,通过方法名和参数,就可以完成一些简单的crud
```

#### 定义实体类Movie

``` java
@Document(indexName = "item",type = "docs", shards = 1, replicas = 0)
//indexName:索引库名，个人建议以项目名称命名
//type:类型，个人建议以实体类名称命名
//shards 分片;replicas 副本
public class Movie {

    @Id  //主键
    private String id;

    //ik_max_word  最大程度分词,分词很细
    //ik_small  智能分词,分词不会太细
    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String name;

    //FieldType.keyword 不分词,没个字都要拆开
    //FieldType.text  分词,实际情况(ik分词器)来拆分
    @Field(type = FieldType.keyword)
    private String type;//惊悚 悬疑 喜剧 动作 科幻 动画 英语剧 舞台剧

    @Field(type = FieldType.keyword)
    private String director;//导演

    @Field(type = FieldType.keyword)
    private String actor;//代表演员

    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String brief;//电影简述

    //搜索时,不会作为条件的字段,可以加上:index = false,即不会以文件路径作为搜索的参数
    @Field(index = false, type = FieldType.keyword)
    private String filePath;

    @Field(index = false, type = FieldType.Integer)
    private int favCount;
    //查询index=false的字段,会返回如下异常
    //IllegalArgumentException[Cannot search on field [favCount] since it is not indexed
    @Field(index = false, type = FieldType.keyword)
    private String createDate;
}
```

**注:**

http://localhost:9200/_analyze?analyzer=ik_max_word&text=胡迪深知自己在这个世界上的使命

http://localhost:9200/_analyze?analyzer=ik_smart&text=胡迪深知自己在这个世界上的使命

访问以上的两个地址,可以体会max分词和small分词

#### 定义Repository

```java
//Movie:为实体类
//String:为Movie实体类中主键的数据类型
@Repositorypublic 
interface MovieRepository extends ElasticsearchRepository<Movie,String> {
    
}
```

**ElasticsearchRepository**即spring data Elasticsearch定义的对es库进行基本增删改查的类,该类定义了很多基本方法,可以直接调用,完成简单的增删改查;

当然,你也可以按照一些规则,完成一些复查的查询,参考博客:https://blog.csdn.net/chen_2890/article/details/83895646

![](img\2019-10-05_152238.gif)

#### 新建索引库,插入数据

**org.springframework.data.elasticsearch.core.ElasticsearchTemplate**,可以让我:

创建索引的API

```java
esTemplate.createIndex(Movie.class);
```

映射相关的API

删除索引的API

```java
esTemplate.deleteIndex(Movie.class)  可以根据类和索引名删除索引
esTemplate.deleteIndex("item")
```

创建索引后,是数据的插入

![](img\2019-10-05_153121.gif)

### es数据搜索操作(java代码及接口请求访问)

http://localhost:9200/item,可以查看item索引库的一些基本信息,字段映射相关信息

http://localhost:9200/item/docs/_search/,http://localhost:9200/{索引库名}/{类型}/_search,是我们进行接口请求访问数据的关键方式,效果如下:

请求参数:

``` json
{
    "query": {
        "bool": {
            "must": {
                "match": {
                    "name": "轮到你了"
                }
            }
        }
    }
}
```

接口响应

``` json
{
    "took": 17,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "failed": 0
    },
    "hits": {
        "total": 1,
        "max_score": 3.878859,
        "hits": [
            {
                "_index": "item",
                "_type": "docs",
                "_id": "9ff2152a3cbf4d9485ee012da71f711c",
                "_score": 3.878859,
                "_source": {
                    "id": "9ff2152a3cbf4d9485ee012da71f711c",
                    "name": "轮到你了",
                    "type": "犯罪",
                    "director": "小室直子",
                    "actor": "西野七濑",
                    "brief": "新婚夫妇菜奈（原田知世饰）和翔太（田中圭饰）搬进了公寓的新房，幸福甜蜜的两人对新生活充满期待。搬家当天公寓要开居民会，菜奈猜拳输给了翔太，便独自前去参加。",
                    "filePath": "/file/movie/a.mp4",
                    "favCount": 2300,
                    "createDate": "2019-08-04"
                }
            }
        ]
    }
}
```

讲http://localhost:9200接口访问数据搜索的原因,也是方便大家在java编码前,自己先尝试,因为接口请求参数的规则与java代码请求的规则,是一致

上文的接口请求,下文是java的代码编写

``` java
@Test
public void testboolQuery(){
    // 创建对象
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
                  		queryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name","轮到")));
    Page<Movie> movies = movieRepository.search(queryBuilder.build());
    List<Movie> content = movies.getContent();
    content.stream().forEach(System.out::println);
}
```

**QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name","轮到"))**与上文接口请求参数的编写方式是一致的

#### matchQuery和termQuery

``` java
@Test
public void testMatchQuery(){
    // 创建对象
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    queryBuilder.withQuery(QueryBuilders.matchQuery("name","轮到你"));
    Page<Movie> movies = movieRepository.search(queryBuilder.build());
    List<Movie> content = movies.getContent();
    content.stream().forEach(System.out::println);
}

@Test
public void testTermQuery(){
    //matchQuery:底层就是使用的termQuery
    //termQuery 功能更强大，除了匹配字符串以外，还可以匹配 int/long/double/float/
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    queryBuilder.withQuery(QueryBuilders.termQuery("favCount",800));
    Page<Movie> movies = movieRepository.search(queryBuilder.build());
    List<Movie> content = movies.getContent();
    content.stream().forEach(System.out::println);
}
```

**关于matchQuery和termQuery的区别:**

matchQuery:会将查询的字段进行分词,用分好的词去匹配索引库,返回查询结果

termQuery:不处理查询的字段,直接去匹配索引库,返回查询结果

我们已近期比较火的日剧,**轮到你了**来举例

http://localhost:9200/_analyze?analyzer=ik_max_word&text=轮到你了

``` json
{
    "tokens": [
        {
            "token": "轮到",
            "start_offset": 0,
            "end_offset": 2,
            "type": "CN_WORD",
            "position": 0
        },
        {
            "token": "到你",
            "start_offset": 1,
            "end_offset": 3,
            "type": "CN_WORD",
            "position": 1
        },
        {
            "token": "了",
            "start_offset": 3,
            "end_offset": 4,
            "type": "CN_CHAR",
            "position": 2
        }
    ]
}
```

**termQuery**的效果

![](img\2019-10-06_074438.gif)

**matchQuery**的效果

![](img\2019-10-06_074556.gif)

都是搜索 "轮到你了",**termQuery没有结果,而matchQuery确有结果**;显然,ik分词器分词时,并没有给 "轮到你了",这个词建立索引,termQuery对查询参数不做处理,"轮到你了"匹配不到任何索引,故无结果;但是matchQuery自带为查询参数分词的效果,将查询条件的"轮到你了"分词,这些分词匹配到了索引库中的"轮到","你了"这些字段,故返回了结果;

如上特性也只:matchQuery适合中一些模糊的,需要分词的查询,被text修饰的字段;而termQuery适合做一些较为准确的查询,比如数字查询,名称查询等被keyword修饰的字段

``` 
注:上文声明时,我将favCount字段修饰为index=false,故我在搜索favCount时,报了IllegalArgumentException[Cannot search on field [favCount] since it is not indexed,我就不改了,要查询的字段,记得不要设置为false
```

#### matchQuery与matchPhraseQuery

说法一:

```
matchPhraseQuery和matchQuery等的区别，在使用matchQuery等时，在执行查询时，搜索的词会被分词器分词，而使用matchPhraseQuery时，不会被分词器分词，而是直接以一个短语的形式查询，而如果你在创建索引所使用的field的value中没有这么一个短语（顺序无差，且连接在一起），那么将查询不出任何结果。
```

说法二:

```
和match查询类似，match_phrase查询首先解析查询字符串来产生一个词条列表。然后会搜索所有的词条，但只保留包含了所有搜索词条的文档，并且词条的位置要邻接
```

网上有多种方法,不会分词,会分词,但是 我自己实尝试,用"轮到你了"搜索.termQuery无结果而matchPhraseQuery确实有结果.故说法二应该是对的

**有争议,说明有坑,研究一下这个查询**

**matchQuery**

``` java
queryBuilder.withQuery(QueryBuilders.matchQuery("name","轮到大家"));//有结果
```

结论:matchQuery,查询参数分词,只要分词匹配到索引库,就返回该记录

**matchPhraseQuery**

``` java
queryBuilder.withQuery(QueryBuilders.matchPhraseQuery("name","轮到大家"));//无结果
```

结论:matchPhraseQuery,查询参数分词,但是,**然后会搜索所有的词条，但只保留包含了所有搜索词条的文档，并且词条的位置要邻接**;虽然通过"轮到"匹配到了词条,但是这条记录并没有包含"轮到大家"这个搜索条件,故也被排除;

**matchPhraseQuery查询结果比matchQuery更加精确,一般而言,会过滤掉一些matchQuery搜索出的结果,使得搜索结果更精确**

#### boolQuery

Bool查询现在包括四种子句,must,filter,should,must_not。

query的时候，会先比较查询条件，然后计算分值，最后返回文档结果；

filter则是先判断是否满足查询条件，如果不满足，会缓存查询过程（记录该文档不满足结果）；满足的话，就直接缓存结果。

综上所述，filter快在两个方面：

- 1 对结果进行缓存
- 2 避免计算分值

Bool查询对应Lucene中的BooleanQuery，它由一个或者多个子句组成，每个子句都有特定的类型。

##### must

返回的文档必须满足must子句的条件，并且参与计算分值

##### filter

返回的文档必须满足filter子句的条件。但是不会像Must一样，参与计算分值

##### should

返回的文档可能满足should子句的条件。在一个Bool查询中，**如果没有must或者filter**，**有一个或者多个should子句**，那么只要满足一个就可以返回。`minimum_should_match`参数定义了至少满足几个子句。

##### must_nout

返回的文档必须不满足must_not定义的条件。

``` java
 @Test
public void testboolQuery2(){
    // 构建查询条件
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    QueryBuilder queryBuilder1=QueryBuilders.matchPhraseQuery("name", "轮到你了");
    QueryBuilder queryBuilder2=QueryBuilders.matchPhraseQuery("type", "轮到你了");
    QueryBuilder queryBuilder3=QueryBuilders.matchPhraseQuery("director", "轮到你了");
    QueryBuilder queryBuilder4=QueryBuilders.matchPhraseQuery("actor", "轮到你了");
    QueryBuilder queryBuilder5=QueryBuilders.matchPhraseQuery("brief", "轮到你了");
    //比较时间
    //QueryBuilder queryBuilder3=QueryBuilders.rangeQuery("publishDate").gt("2018-01-01");
    queryBuilder.withQuery(QueryBuilders.boolQuery().should(queryBuilder1)
                           .should(queryBuilder2).should(queryBuilder3).should(queryBuilder4).should(queryBuilder5).minimumShouldMatch(2));
    //minimumShouldMatch为 1 时,有查询结果
    //minimumShouldMatch为 2 时,无查询结果
    //分页查询
    Page<Movie> movies = movieRepository.search(queryBuilder.build());
    List<Movie> content = movies.getContent();
    content.stream().forEach(System.out::println);
}
```

### fuzzyQuery



### wildcardQuery



### regexQuery



## es分值计算