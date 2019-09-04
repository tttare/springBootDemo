package com.tttare.springDemo.userCenter.service;

import com.tttare.springDemo.model.LoginResult;
import org.apache.shiro.session.Session;

/**
 * ClassName: LoginService <br/>
 * Description: <br/>
 * date: 2019/9/3 12:23<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public interface LoginService {

    LoginResult login(String userName, String password);

    void logout();

    String getCurrentUserName();

    Session getSession();
}
