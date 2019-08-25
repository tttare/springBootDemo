package com.tttare.springDemo.userCenter.contorller;

import com.tttare.springDemo.model.User;
import com.tttare.springDemo.userCenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Log
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index(){
        return "HelloWorld!";
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<User> listAllUser(){
        return userService.findAllUser();

    }

    @RequestMapping("/listPage")
    public String userListPage(Model model){
        List<User> userList = userService.findAllUser();
        model.addAttribute("userList",userList);
        return "/user/userList";
    }
}
