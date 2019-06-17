package com.springboot.mybatisdemo.controller.advice;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定义控制器通知，增强控制器
 */

@ControllerAdvice(
        //指定拦截的包
        basePackages = {""},
        //限定标注为@Controller的类才被拦截
        annotations = Controller.class)
public class MyControllerAdvice {

    // 绑定格式化、参数转换规则和增加验证器等
    @InitBinder
    public void initDataBinder(WebDataBinder binder) {
        // 自定义日期编辑器，限定格式为 yyyy-MM-dd，且参数不允许为空
        CustomDateEditor dateEditor =
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false);
        // 注册自定义日期编辑器
        binder.registerCustomEditor(Date.class, dateEditor);
    }

    // 在执行控制器之前先执行，可以初始化数据模型
    @ModelAttribute
    public void projectModel(Model model) {
        model.addAttribute("project_name", "chapter10");
    }

    // 异常处理，使得被拦截的控制器方法发生异常时，都能用相同的视图响应
    @ExceptionHandler(value = Exception.class)
    // 定义为服务器错误状态码
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Model model, Exception ex) {
        // 给数据模型增加异常消息
        model.addAttribute("exception_message", ex.getMessage());
        // 返回异常视图
        return "exception";
    }
}
