package com.springboot.mybatisdemo.controller;

import com.springboot.mybatisdemo.pojo.User;
import com.springboot.mybatisdemo.service.MyBatisUserService;
import com.springboot.mybatisdemo.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MyBatisUserService userService = null;

    @GetMapping("/header/page")
    public String headerPage() {
        return "header";
    }

    @PostMapping("/header/user")
    @ResponseBody
// 通过@RequestHeader接收请求头参数
    public User headerUser(@RequestHeader("id") Long id) {
        User user = userService.getUser(id);
        return user;
    }

    @RequestMapping("/details")
    public ModelAndView detail(Long id){
        User user = userService.getUser(id);

        //模型和视图
        ModelAndView mv = new ModelAndView();
        //定义模型视图
        mv.setViewName("user/details");
        //加入数据模型
        mv.addObject(user);

        return mv;
    }

    /**
     * 使用Json视图
     * @param id
     * @return
     */
    @RequestMapping("/detailsForJson")
    public ModelAndView detailsForJson(@RequestParam(value = "id") Long id){
        User user = userService.getUser(id);

        ModelAndView mv = new ModelAndView();
        //生成Json视图
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        mv.setView(jsonView);

        mv.addObject(user);
        return mv;
    }

    /**
     * 新增用户
     * @param user 通过@RequestBody 注解得到 JSON 参数
     * @return 回填 id 后的用户信息
     */
    @PostMapping("/insert")
    @ResponseBody
    // 指定状态码为201（资源创建成功）
    @ResponseStatus(HttpStatus.CREATED)
    public User insert(@RequestBody User user) {
        userService.insertUser(user);
        return user;
    }

    /**
     * 通过 URL 传递参数
     * 示例：http://localhost:8080/user/1
     * @param id
     * @return
     */
    //{...}代表占位符，还可以配置参数名称
    @GetMapping("/{id}")
    //响应Json数据集
    @ResponseBody
    //@PathVariable 通过名称获取参数
    public User get(@PathVariable("id") Long id){
        return userService.getUser(id);
    }

    /**
     * 获取格式化参数,如日期和货币
     * @param date
     * @param number
     * @return
     */
    @PostMapping("/format/commit")
    @ResponseBody
    public Map<String, Object> format(
            @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) Date date,
            @NumberFormat(pattern = "#,###.##") Double number) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("date", date);
        dataMap.put("number", number);
        return dataMap;
    }

    /**
     * 调用控制前先执行@InitBinder标注的这个方法
     * 验证参数是否正确
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder){
        //绑定验证器
        binder.setValidator(new UserValidator());

        //定义日期参数格式，参数不再需要注解@DateTimeFormat，boolean 参数表示是否允许为空
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyy-MM-dd"), false));
    }

    /**
     *
     * @param user -- 用户对象用 StringToUserConverter 转换
     * @param Errors --验证器返回的错误
     * @param date -- 因为 WebDataBinder 已经绑定了格式，所以不再需要注解
     * @return 各类数据
     */
    @GetMapping("/validator")
    @ResponseBody
    public Map<String, Object> validator(@Valid User user,
                                         Errors Errors, Date date) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("date", date);
        // 判断是否存在错误
        if (Errors.hasErrors()) {
            // 获取全部错误
            List<ObjectError> oes = Errors.getAllErrors();
            for (ObjectError oe : oes) {
                // 判定是否字段错误
                if (oe instanceof FieldError) {
                    // 字段错误
                    FieldError fe = (FieldError) oe;
                    map.put(fe.getField(), fe.getDefaultMessage());
                } else {
                    // 对象错误
                    map.put(oe.getObjectName(), oe.getDefaultMessage());
                }
            }
        }
        return map;
    }

}
