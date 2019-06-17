package com.springboot.mybatisdemo.controller;

import com.springboot.mybatisdemo.pojo.TestPojo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mytest")
public class MyTest {

    @GetMapping("/add")
    public String add() {
        return "/user/add";
    }

    @RequestMapping("/insert")
    @ResponseBody
    public TestPojo setPojo(@RequestBody TestPojo pojo){
        return pojo;
    }

    @GetMapping("/set")
    @ResponseBody
    public String setPojo(String name){
        return name;
    }
}
