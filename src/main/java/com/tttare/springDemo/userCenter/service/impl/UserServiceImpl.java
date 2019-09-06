package com.tttare.springDemo.userCenter.service.impl;


import com.tttare.springDemo.userCenter.dao.UserMapper;
import com.tttare.springDemo.userCenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.tttare.springDemo.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAllUser(){
        return userMapper.listAllUser();
    }

    @Override
    public User findByUserName(String userName) {
        return userMapper.findByUserName(userName);
    }

    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
    }
}
