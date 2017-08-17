package com.zs.controller;

import com.alibaba.fastjson.JSONObject;
import com.zs.bean.User;
import com.zs.service.UserService;
import com.zs.type.Date4LongType;
import com.zs.type.ListStrType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by zhangshuqing on 2017/8/3.
 */

@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger= LoggerFactory.getLogger(this.getClass());



    @Autowired
    UserService userService;
    @RequestMapping("/index")
    public String index(Model model) {
        User user=new User();
        user.setUserName("小明");
        user.setAge(20);
        user.setAddress("广东省深圳市宝安区");
        Date4LongType d4t=new Date4LongType();
        d4t.setCurrentTime(new Date());
        user.setRegTime(d4t);
        ListStrType lst=new ListStrType();
        List array=new ArrayList();
        array.add("足球");
        array.add("篮球");
        array.add("羽毛球");
        lst.setValues(array);
        user.setInterest(lst);
        userService.inserUser(user);
        model.addAttribute("name","zhangshuqing");
        return "index";
    }

    @RequestMapping("/info")
    @ResponseBody
    public Object info(){
        logger.info("request [/info]");
        Map<String,Object> map=new HashMap<>();
        map.put("userName","zs");
        map.put("age",20);
        List<User> userList=userService.findAllUser();
        System.out.println(userList);
        map.put("userList",userList);
        return map;
    }


    @Value("${app.name}")
    String appName;

    @Value("${app.version}")
    String appVersion;

    @RequestMapping("/appVersion")
    @ResponseBody
    public JSONObject appVersion(){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("appName",appName);
        jsonObject.put("appVersion",appVersion);
        return jsonObject;

    }
    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        userService.test();
        return "sdfsdfsd";
    }



}
