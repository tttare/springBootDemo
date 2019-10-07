package test;

import com.tttare.elasticsearch.ElasticsearchApplication;
import com.tttare.elasticsearch.api.dao.MovieRepository;
import com.tttare.elasticsearch.model.Movie;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * ClassName: EsApiTest <br/>
 * Description: <br/>
 * date: 2019/10/6 4:19<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@SpringBootTest(classes = ElasticsearchApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class EsApiTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testMatchQuery(){
        // 创建对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchPhraseQuery("name","轮到你"));
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

    @Test
    public void testboolQuery(){
        // 创建对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery("name","轮"))
                .should(QueryBuilders.matchPhraseQuery("brief","翔太")));
        Page<Movie> movies = movieRepository.search(queryBuilder.build());
        List<Movie> content = movies.getContent();
        content.stream().forEach(System.out::println);
    }

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
        //分页查询
        Page<Movie> movies = movieRepository.search(queryBuilder.build());
        List<Movie> content = movies.getContent();
        content.stream().forEach(System.out::println);
    }

    @Test
    //模糊查询
    public void testFuzzyQuery(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.fuzzyQuery("name","轮"));
        Page<Movie> movies = movieRepository.search(builder.build());
        List<Movie> content = movies.getContent();
        content.stream().forEach(System.out::println);
    }


}
