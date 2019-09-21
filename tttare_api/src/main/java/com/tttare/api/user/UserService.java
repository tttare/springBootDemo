package com.tttare.api.user;

import com.tttare.springDemo.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ClassName: UserService <br/>
 * Description: <br/>
 * date: 2019/9/10 8:57<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@FeignClient(value="tttare-core")
public interface UserService {

    @RequestMapping(method=RequestMethod.GET,value = "/user/{userName}")
    User findByName(@PathVariable("userName") String userName);
}
