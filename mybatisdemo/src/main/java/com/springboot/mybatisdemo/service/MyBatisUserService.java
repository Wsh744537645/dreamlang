package com.springboot.mybatisdemo.service;


import com.springboot.mybatisdemo.pojo.User;

import java.util.List;

public interface MyBatisUserService {
    User getUser(Long id);

    User insertUser(User user);

    // 修改用户，指定MyBatis的参数名称
    User updateUserName(Long id, String userName);

    // 查询用户，指定MyBatis的参数名称
    List<User> findUsers(String userName, String note);

    // 删除用户
    int deleteUser(Long id);
}
