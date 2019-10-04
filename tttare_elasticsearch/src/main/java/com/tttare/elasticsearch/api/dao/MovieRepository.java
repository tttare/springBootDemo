package com.tttare.elasticsearch.api.dao;

import com.tttare.elasticsearch.model.Movie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * ClassName: MovieRepository <br/>
 * Description: 定义ItemRepository 接口<br/>
 * date: 2019/10/4 10:49<br/>
 * Movie:为实体类
 * String:为Movie实体类中主键的数据类
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Repository
public interface MovieRepository extends ElasticsearchRepository<Movie,String> {
}
