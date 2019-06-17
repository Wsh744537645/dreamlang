package com.springboot.mybatisdemo.controller;

import com.springboot.mybatisdemo.pojo.User;
import com.springboot.mybatisdemo.service.MyBatisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/mybatis")
public class MyBatisController {
    @Autowired
    private MyBatisUserService myBatisUserService;

    @RequestMapping("/getUser")
    @ResponseBody
    public User getUser(Long id){
        return myBatisUserService.getUser(id);
    }

    @RequestMapping("insertUser")
    @ResponseBody
    public User insertUser(String userName, String note){
        User user = new User();
        user.setUserName(userName);
        user.setNote(note);
        myBatisUserService.insertUser(user);
        return user;
    }
}
