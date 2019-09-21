package com.tttare.portal.user.controller;

import com.tttare.api.user.UserService;
import com.tttare.springDemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: UserController <br/>
 * Description: <br/>
 * date: 2019/9/10 10:08<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("index")
    public User index(){
        return userService.findByName("test");
    }

}
