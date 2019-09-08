package com.tttare.springDemo.userCenter.service.impl;

import com.tttare.springDemo.common.cache.IRedis;
import com.tttare.springDemo.common.model.Contant;
import com.tttare.springDemo.common.model.ResponseParam;
import com.tttare.springDemo.common.utils.EmailUtil;
import com.tttare.springDemo.common.utils.RandomUtils;
import com.tttare.springDemo.common.utils.RedisUtil;
import com.tttare.springDemo.model.LoginResult;
import com.tttare.springDemo.userCenter.dao.UserMapper;
import com.tttare.springDemo.userCenter.service.LoginService;
import com.tttare.springDemo.userCenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * ClassName: LoginServiceImpl <br/>
 * Description: <br/>
 * date: 2019/9/3 23:46<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Resource(name = "redisUtil")
    private IRedis redisUtil;

    @Override
    public LoginResult login(String userName, String password) {
        LoginResult loginResult = new LoginResult();
        if(userName==null || userName.isEmpty())
        {
            loginResult.setLogin(false);
            loginResult.setResult("用户名为空");
            return loginResult;
        }
        String msg="";
        // 1、获取Subject实例对象
        Subject currentUser = SecurityUtils.getSubject();

//        // 2、判断当前用户是否登录
//        if (currentUser.isAuthenticated() == false) {
//
//        }

        // 3、将用户名和密码封装到UsernamePasswordToken
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        // 4、认证
        try {
            currentUser.login(token);// 传到MyAuthorizingRealm类中的方法进行认证
            Session session = currentUser.getSession();
            session.setAttribute("userName", userName);
            loginResult.setLogin(true);
            return loginResult;
            //return "/index";
        }catch (UnknownAccountException e)
        {
            e.printStackTrace();
            msg = "账号不存在!";
        }
        catch (IncorrectCredentialsException e)
        {
            msg = "密码不正确!";
        }
        catch (AuthenticationException e) {
            e.printStackTrace();
            msg="用户验证失败!";
        }

        loginResult.setLogin(false);
        loginResult.setResult(msg);

        return loginResult;
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Override
    public String getCurrentUserName() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return (String)session.getAttribute("userName");
    }

    @Override
    public Session getSession() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return session;
    }

    @Override
    public ResponseParam confirmEmail(Map<String,String> params) {
        // 判断邮箱是否已经被使用
        int num = userMapper.countByCondition(params);
        if(num > 0){
            return new ResponseParam(Contant.FAIL,"该邮箱已被注册使用,请更换邮箱");
        }
        String email = params.get("email");
        String code = RandomUtils.generateString(6);
        try {
            EmailUtil.send(email,"邮箱验证",code);
        }catch (Exception e){
            log.error("email post fail:"+e.getMessage());
            return new ResponseParam(Contant.FAIL,"抱歉,邮箱验证失败,请稍后重试");
        }
        long times=60*5;//五分钟后过期
        redisUtil.setObject(email,code,times);
        return new ResponseParam(Contant.SUCCESS,"邮件验证成功");
    }
}
