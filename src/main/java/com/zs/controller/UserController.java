package com.zs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangshuqing on 2017/8/3.
 */

@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger= LoggerFactory.getLogger(this.getClass());
    @RequestMapping("/index")
    public String index(Model model) {
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
        return map;
    }
}
