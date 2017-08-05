package com.zs.bean;

import com.zs.type.Date4LongType;
import com.zs.type.ListStrType;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-04
 * Time: 17:40
 */
public class User {
    private Long id;
    private String userName;
    private int age;
    private String address;
    private ListStrType interest;
    private Date4LongType regTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ListStrType getInterest() {
        return interest;
    }

    public void setInterest(ListStrType interest) {
        this.interest = interest;
    }

    public Date4LongType getRegTime() {
        return regTime;
    }

    public void setRegTime(Date4LongType regTime) {
        this.regTime = regTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
