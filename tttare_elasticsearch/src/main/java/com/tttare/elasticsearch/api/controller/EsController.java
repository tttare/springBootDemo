package com.tttare.elasticsearch.api.controller;

import com.tttare.elasticsearch.api.service.MovieService;
import com.tttare.elasticsearch.model.Movie;
import com.tttare.springDemo.model.ResponseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: EsController <br/>
 * Description: <br/>
 * date: 2019/10/3 23:06<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Slf4j
@Controller
public class EsController {

    @Autowired
    private MovieService movieService;

    @RequestMapping("/initial")
    @ResponseBody
    public ResponseParam initial(){
        try {
            movieService.initial();
            return new ResponseParam("000000","success");
        }catch (Exception e){
            log.error("es数据初始化异常:"+e.getMessage());
            return new ResponseParam("000001",e.getMessage().substring(0,100));
        }
    }

    @RequestMapping("/findMovies")
    @ResponseBody
    public ResponseParam findMovies(String name,int page,int size){
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Movie> movies = movieService.findMovie(name, pageRequest);
            return new ResponseParam("000000",movies,"success");
        }catch (Exception e){
            log.error("es数据初始化异常:"+e.getMessage());
            return new ResponseParam("000001",e.getMessage().substring(0,100));
        }
    }

}
