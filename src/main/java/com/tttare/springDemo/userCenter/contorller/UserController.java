package com.tttare.springDemo.userCenter.contorller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tttare.springDemo.common.cache.IRedis;
import com.tttare.springDemo.common.utils.FileViewUtil;
import com.tttare.springDemo.common.utils.HttpUtil;
import com.tttare.springDemo.model.FileObject;
import com.tttare.springDemo.model.User;
import com.tttare.springDemo.userCenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Log
@Controller
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Resource(name = "redisUtil")
    private IRedis redisUtil;

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

    @RequestMapping("/viewFile")
    public String viewFile(Model model){
        FileObject fo = new FileObject();
        File file = new File("E:\\书籍\\tttare");
        fo.setId("top");
        FileObject fileObject = FileViewUtil.getSubFile(file, fo,0);
        String str = JSONObject.toJSONString(fileObject);
        model.addAttribute("fileObject",str);
        return "/user/fileList";
    }

    @RequestMapping("/viewFileJson")
    @ResponseBody
    public FileObject viewFileJson(Model model,HttpServletResponse response) throws Exception {
        FileObject fObj = redisUtil.getObject("viewFileObj",FileObject.class);
        if(fObj==null){
            FileObject fo = new FileObject();
            fo.setId("top");
            File file = new File("E:\\书籍\\tttare");
            fObj = FileViewUtil.getSubFile(file, fo,0);
            redisUtil.setObject("viewFileObj",fObj,null);
        }
        return fObj;
    }

    @RequestMapping("/viewVideo")
    public String viewVideo(Model model){

        return "/common/videoPlay";
    }
}
