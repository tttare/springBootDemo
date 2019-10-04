package com.tttare.elasticsearch.api.service.impl;

import com.tttare.elasticsearch.api.dao.MovieRepository;
import com.tttare.elasticsearch.api.service.MovieService;
import com.tttare.elasticsearch.model.Movie;
import com.tttare.springDemo.tools.CommonUtil;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * ClassName: MovieServiceImpl <br/>
 * Description: <br/>
 * date: 2019/10/4 15:44<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void initial() {
        //创建索引库
        esTemplate.createIndex(Movie.class);
        //esTemplate.deleteIndex(Movie.class)  可以根据类和索引名删除索引
        //插入测试数据
        Movie m1 = new Movie(CommonUtil.getUUID(),"我和我的祖国","剧情","陈凯歌","黄渤","七位导演分别取材新中国成立70周年以来，祖国经历的无数个历史性经典瞬间。讲述普通人与国家之间息息相关密不可分的动人故事。聚焦大时代大事件下，小人物和国家之间，看似遥远实则密切的关联，唤醒全球华人共同回忆。","/file/movie/a.mp4",1000,"2019-08-01");
        Movie m2 = new Movie(CommonUtil.getUUID(),"轮到你了","犯罪","小室直子","西野七濑","新婚夫妇菜奈（原田知世饰）和翔太（田中圭饰）搬进了公寓的新房，幸福甜蜜的两人对新生活充满期待。搬家当天公寓要开居民会，菜奈猜拳输给了翔太，便独自前去参加。","/file/movie/a.mp4",2300,"2019-08-04");
        Movie m3 = new Movie(CommonUtil.getUUID(),"电影少女2018","爱情","関和亮","西野七濑","普通高中生弄内翔（野村周平 饰）因父母离婚而住进了当绘本作家的叔叔弄内洋太的空房子，开始一个人的生活。翔暗恋同班同学奈奈美，可奈奈美却对翔的好朋友智章芳心暗许。一天，翔偶然在家发现了一台坏的录像机，里面还插着一盒录像带。他修好录像带后按下播放键，一个自称“VIDEO girl·天野爱（西野七濑 饰）”的女孩居然中屏幕里走了出来。于是，翔和爱在同一屋檐下开始了一段三个月的奇妙生活。","/file/movie/a.mp4",5000,"2019-08-09");
        Movie m4 = new Movie(CommonUtil.getUUID(),"寄生虫","犯罪","奉俊昊","寄生虫","基宇（崔宇植 饰）出生在一个贫穷的家庭之中，和妹妹基婷（朴素丹 饰）以及父母在狭窄的地下室里过着相依为命的日子。一天，基宇的同学上门拜访，他告诉基宇，自己在一个有钱人家里给他们的女儿做家教，太太是一个头脑简单出手又阔绰的女人，因为自己要出国留学，所以将家教的职位暂时转交给基宇。","/file/movie/a.mp4",800,"2019-09-01");
        Movie m5 = new Movie(CommonUtil.getUUID(),"玩具总动员4","d动画","乔什·库雷","汤姆·汉克斯","在多年的独自闯荡中，牧羊女已经变得热爱冒险，不再只是一个精致的洋娃娃。正当胡迪和牧羊女发现彼此对玩具的使命的意义大相径庭时，他们很快意识到更大的威胁即将到来……","/file/movie/a.mp4",900,"2019-07-01");
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(m1);
        movies.add(m2);
        movies.add(m3);
        movies.add(m4);
        movies.add(m5);
        movieRepository.saveAll(movies);
    }

    // 条件分页查询,这个name将是完全的模糊查询条件
    // 匹配 电影名 类型 导演 演员 简介
    @Override
    public Page<Movie> findMovie(String name, Pageable page) {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        QueryBuilder queryBuilder1=QueryBuilders.matchPhraseQuery("name", name);
        QueryBuilder queryBuilder2=QueryBuilders.matchPhraseQuery("type", name);
        QueryBuilder queryBuilder3=QueryBuilders.matchPhraseQuery("director", name);
        QueryBuilder queryBuilder4=QueryBuilders.matchPhraseQuery("actor", name);
        QueryBuilder queryBuilder5=QueryBuilders.matchPhraseQuery("brief", name);
        //分页
        queryBuilder.withPageable(page);
        //比较时间
        //QueryBuilder queryBuilder3=QueryBuilders.rangeQuery("publishDate").gt("2018-01-01");
        queryBuilder.withQuery(QueryBuilders.boolQuery().should(queryBuilder1)
                .should(queryBuilder2).should(queryBuilder3).should(queryBuilder4).should(queryBuilder5));
        //分页查询
        Page<Movie> movies = movieRepository.search(queryBuilder.build());
        return movies;
    }


}
