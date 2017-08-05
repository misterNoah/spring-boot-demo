package com.zs.service;

import com.zs.bean.User;
import com.zs.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-05
 * Time: 9:46
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void inserUser(User user){
        userMapper.insertUser(user);
    }

    public List<User> findAllUser(){
        return userMapper.findAllUser();
    }
}
