package com.zs.repository;

import com.zs.bean.User;

import java.util.List;

/**
 * Created by zhangshuqing on 2017/8/4.
 */


public interface UserMapper {

    List<User> findAllUser();

    void insertUser(User user);

}
