package com.tttare.springDemo.userCenter.contorller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.tttare.springDemo.common.cache.IRedis;
import com.tttare.springDemo.common.model.Contant;
import com.tttare.springDemo.common.model.ResponseParam;
import com.tttare.springDemo.common.utils.AesUtils;
import com.tttare.springDemo.common.utils.CommonUtil;
import com.tttare.springDemo.common.utils.EncryptUtils;
import com.tttare.springDemo.common.utils.RandomUtils;
import com.tttare.springDemo.model.LoginResult;
import com.tttare.springDemo.model.User;
import com.tttare.springDemo.userCenter.service.LoginService;
import com.tttare.springDemo.userCenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: LoginContorller <br/>
 * Description: user login Controller<br/>
 * date: 2019/9/4 21:57<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Slf4j
@Controller
@RequestMapping(value="/user")
public class LoginContorller {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Resource(name = "redisUtil")
    private IRedis redisUtil;

    @Resource
    DefaultKaptcha defaultKaptcha;

    //md5加密
    private String algorithmName = "md5";
    private int hashIterations = 2;
    //密码盐
    private String salt = "8d78869f470951332959580424d4bf4f";


    private long verifyTTL = 60;//验证码过期时间60秒

    private String create16String()
    {
        return RandomUtils.generateString(16);
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String toLogin(Map<String, Object> map,HttpServletRequest request){
        loginService.logout();
        String key = create16String();
        map.put("key",key);
        return "/user/login";
    }

    /**
     * 2、生成验证码
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getVerifyCode")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        byte[] bytesCaptchaImg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            request.getSession().setAttribute("verifyCode", createText);
            request.getSession().setAttribute("verifyCodeTTL", System.currentTimeMillis());
            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage bufferedImage = defaultKaptcha.createImage(createText);
            ImageIO.write(bufferedImage, "jpg", jpegOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        bytesCaptchaImg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(bytesCaptchaImg);
        responseOutputStream.flush();

        responseOutputStream.close();
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(HttpServletRequest request) throws Exception {
        System.out.println("login()");
        Map<String, Object> map = new HashMap<>();
        String userName = request.getParameter("userName");
        String encryptedPassword = request.getParameter("password");
        String key = request.getParameter("key");

        String verifyCode = request.getParameter("verifyCode");
        String rightCode = (String) request.getSession().getAttribute("verifyCode");
        Long verifyCodeTTL = (Long) request.getSession().getAttribute("verifyCodeTTL");

        String password = AesUtils.decrypt(encryptedPassword,key);

        Long currentMillis = System.currentTimeMillis();
        if (rightCode == null || verifyCodeTTL == null) {
            map.put("msg", "请刷新图片，输入验证码！");
            map.put("userName", userName);
            map.put("success",false);
            map.put("url","/user/login");
            return map;
        }
        Long expiredTime = (currentMillis - verifyCodeTTL) / 1000;
        if (expiredTime > this.verifyTTL) {
            map.put("msg", "验证码过期，请刷新图片重新输入！");
            map.put("userName", userName);
            map.put("success",false);
            map.put("url","/user/login");
            return map;
        }

        if (!verifyCode.equalsIgnoreCase(rightCode)) {
            map.put("msg", "验证码错误，请刷新图片重新输入！");
            map.put("userName", userName);
            map.put("success",false);
            map.put("url","/user/login");
            return map;
        }

        LoginResult loginResult = loginService.login(userName, password);
        if (loginResult.isLogin()) {
            //将用户信息存入session
            //获取已登录的用户信息

            map.put("userName", userName);
            map.put("success",true);
            map.put("url","/index");
            return map;
        } else {
            map.put("msg", loginResult.getResult());
            map.put("userName", userName);
            map.put("success",false);
            map.put("url","/user/login");
            return map;
        }
    }

    //用户注册
    @RequestMapping(value = "/logon", method = RequestMethod.GET)
    public String toLogon(HttpServletRequest request){

        return "/user/logon";
    }

    //用户注册
    @RequestMapping(value = "/logon", method = RequestMethod.POST)
    @ResponseBody
    public ResponseParam logon(HttpServletRequest request){
        ResponseParam rp =null;
        String userName = request.getParameter("userName");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String emailCode = request.getParameter("emailCode");
        String emailCodeCache = redisUtil.getObject(email, String.class);
        if(StringUtils.isEmpty(emailCodeCache)){
            rp = new ResponseParam(Contant.FAIL,"验证码已过期");
        }else{
            if(emailCodeCache.equals(emailCode)){
                //作废缓存

                //插入用户
                User user = new User();
                user.setUserId(CommonUtil.getUUID());
                user.setUserName(userName);
                user.setEmail(email);
                user.setSalt(salt);
                user.setState(0);
                Date date = new Date();
                user.setCreateDate(new Timestamp(date.getTime()));
                // 所有用户默认只有一个月有效期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MONTH,1);
                user.setExpiredDate(new Timestamp(calendar.getTime().getTime()));
                String  encryptPwd= EncryptUtils.encrypt(password,user.getCredentialsSalt(),this.algorithmName,this.hashIterations);
                user.setPassword(encryptPwd);
                user.setNickName(name);
                try{
                    userService.addUser(user);
                }catch (Exception e){
                    e.printStackTrace();
                }
                rp = new ResponseParam(Contant.SUCCESS,"注册成功");
            }else{
                rp = new ResponseParam(Contant.FAIL,"验证码已过期");
            }
        }
        return rp;
    }



    //邮箱验证
    @RequestMapping(value = "/confirmEmail", method = RequestMethod.POST)
    @ResponseBody
    public ResponseParam confirmEmail(HttpServletRequest request, @RequestBody Map<String,String> map){
        //检验邮箱是否已被注册
        ResponseParam rp = loginService.confirmEmail(map);
        return rp;
    }
}
