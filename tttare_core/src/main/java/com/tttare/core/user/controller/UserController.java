package com.tttare.core.user.controller;


import com.tttare.springDemo.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: UserController <br/>
 * Description: <br/>
 * date: 2019/9/10 9:32<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@RestController
public class UserController{

    @GetMapping("/user/{userName}")
    public User findUserByUserName(@PathVariable("userName") String userName){
        User user = new User();
        user.setUserId("11111");
        user.setUserName("tttare");
        user.setNickName("爆破酒桶");
        return user;
    }

}
