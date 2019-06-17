package com.springboot.mybatisdemo.service.impl;

import com.springboot.mybatisdemo.pojo.User;
import com.springboot.mybatisdemo.service.MyBatisUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisUserServiceImplTest {
    @Autowired
    private MyBatisUserService userService = null;

    @Test
    public void getUserCache() {
        User user = userService.getUser(1L);
        System.out.println(user);
    }

    @Test
    public void insertUserCache() {
        User user = new User();
        user.setUserName("java");
        user.setNote("开发语言");
        User result = userService.insertUser(user);
        System.out.println(result);
    }

    @Test
    public void updateUserName() {
        User user = userService.updateUserName(7L, "xiaoqi");
        System.out.println(user);
    }

    @Test
    public void findUsers() {
        List<User> userList = userService.findUsers("xiaoqi", "琪琪");
        System.out.println(userList);
    }

    @Test
    public void deleteUser() {
        int result = userService.deleteUser(8L);
        System.out.println(result);
    }
}