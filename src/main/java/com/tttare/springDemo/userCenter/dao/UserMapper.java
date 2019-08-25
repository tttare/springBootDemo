package com.tttare.springDemo.userCenter.dao;

import com.tttare.springDemo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
//@Repository
@Mapper
public interface UserMapper {


    List<User> listAllUser();
}
