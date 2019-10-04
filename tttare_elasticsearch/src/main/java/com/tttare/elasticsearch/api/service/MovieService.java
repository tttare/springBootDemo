package com.tttare.elasticsearch.api.service;

import com.tttare.elasticsearch.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * ClassName: MovieService <br/>
 * Description: <br/>
 * date: 2019/10/4 15:44<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */

public interface MovieService {

    void initial();

    Page<Movie> findMovie(String name, Pageable page);
}
