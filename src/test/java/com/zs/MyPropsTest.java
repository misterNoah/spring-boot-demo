package com.zs;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.bean.MyProps;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshuqing on 2017/8/1.
 */
public class MyPropsTest {

    public static void main(String[] args) {
        List array=new ArrayList();
        array.add("足球");
        array.add("篮球");
        array.add("羽毛球");
        String str=JSON.toJSONString(array);
        System.out.println(str);
    }

}
