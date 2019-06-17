package com.springboot.mybatisdemo.controller;

/**
 * 测试自定义参数转换器 Converter
 * 测试参数验证和自定义参数验证机制 Validator
 */

import com.springboot.mybatisdemo.pojo.User;
import com.springboot.mybatisdemo.pojo.ValidatorPojo;
import com.springboot.mybatisdemo.validator.UserValidator;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("my")
public class myController {

    /**
     * 自定义参数转换接口 StringToUserConverter
     * 测试url：http://localhost:8080/user/converter?user=1-username1-note1
     * @param user
     * @return
     */
    @GetMapping("/converter")
    @ResponseBody
    public User getUserByConverter(User user){
        return user;
    }

    /**
     * 使用集合(List)传递多个用户
     * 测试url：http://localhost:8080/user/list?userList=1-user_name_1-note_1,2-user_name_2-note_2,3-user_name_3-note_3
     * @param userList
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public List<User> list(List<User> userList) {
        return userList;
    }

    @GetMapping("valid/page")
    public String valiPage(){
        return "validator/pojo";
    }

    @RequestMapping("/valid/validate")
    @ResponseBody
    public Map<String, Object> validate(@Valid @RequestBody ValidatorPojo pojo, Errors errors){
        Map<String, Object> errMap = new HashMap<>();

        //获取错误列表
        List<ObjectError> oes = errors.getAllErrors();
        for(ObjectError oe : oes){
            String key = null;
            String msg = null;
            //字段错误
            if(oe instanceof FieldError){
                FieldError fe = (FieldError) oe;
                key = fe.getField();
            }else{
                //非字段错误
                key = oe.getObjectName();
            }

            //错误信息
            msg = oe.getDefaultMessage();
            errMap.put(key, msg);
        }
        System.out.println("errMap" + errMap);
        return errMap;
    }
}
