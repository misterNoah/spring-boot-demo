package com.zs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangshuqing on 2017/8/1.
 */
@SpringBootApplication
@RestController
public class Application{
    private Logger logger= LoggerFactory.getLogger(this.getClass());


    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
