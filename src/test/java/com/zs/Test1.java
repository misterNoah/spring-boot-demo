package com.zs;

import org.junit.Test;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-07
 * Time: 9:52
 */
public class Test1 {

    @Test
    public void test1(){
        Date date=new Date(1501915096569l);
        System.out.println(date.toLocaleString());
    }
}
