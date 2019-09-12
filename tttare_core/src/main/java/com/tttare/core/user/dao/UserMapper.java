package com.tttare.core.user.dao;

import com.tttare.springDemo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserMapper {


    List<User> listAllUser();

    User findByUserName(String userName);

    int countByCondition(Map<String, String> params);

    void addUser(User user);
}
