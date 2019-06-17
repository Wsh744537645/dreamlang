package com.springboot.mybatisdemo.controller;

/**
 * session示例
 */

import com.springboot.mybatisdemo.pojo.User;
import com.springboot.mybatisdemo.service.MyBatisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

// @SessionAttributes指定数据模型名称或者属性类型，保存到 Session中
@SessionAttributes(names = {"user"}, types = Long.class)
@Controller
@RequestMapping("/session")
public class SessionController {
    @Autowired
    private MyBatisUserService userService = null;

    @RequestMapping("/get")
    public String session(){
        return "session";
    }

    @RequestMapping("/test")
    public String test(@SessionAttribute("id") Long id, Model model){
        //根据类型保存到 session 中
        model.addAttribute("id_new", id);
        User user = userService.getUser(id);
        //根据名称保存到session中
        model.addAttribute("user", user);
        return "session/test";
    }
}
