package com.tttare.springDemo.userCenter.service;

import com.tttare.springDemo.model.User;

import java.util.List;
import java.util.Map;


public interface UserService {

    List<User> findAllUser();

    User findByUserName(String userName);

    void addUser(User user);

    //int countByCondition(Map<String,String> );
}
