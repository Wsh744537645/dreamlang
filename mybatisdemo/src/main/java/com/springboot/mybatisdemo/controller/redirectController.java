package com.springboot.mybatisdemo.controller;

/**
 * 重定向示例
 */

import com.springboot.mybatisdemo.pojo.User;
import com.springboot.mybatisdemo.service.MyBatisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/redirect")
public class redirectController {
    @Autowired
    private MyBatisUserService userService = null;

    // 显示用户
// 参数 user 直接从数据模型 RedirectAttributes 对象中取出
    @RequestMapping("/showUser")
    public String showUser(User user, Model model) {
        System.out.println(user.getId());
        return "data/user";
    }

    // 使用字符串指定跳转
    @RequestMapping("/redirect1")
    public String redirect1(String userName, String note, RedirectAttributes ra) {
        User user = new User();
        user.setNote(note);
        user.setUserName(userName);
        userService.insertUser(user);
        // 保存需要传递给重定向的对象
        ra.addFlashAttribute("user", user);
        return "redirect:/user/showUser";
    }

    // 使用模型和视图指定跳转
    @RequestMapping("/redirect2")
    public ModelAndView redirect2(String userName, String note,
                                  RedirectAttributes ra) {
        User user = new User();
        user.setNote(note);
        user.setUserName(userName);
        userService.insertUser(user);
        // 保存需要传递给重定向的对象
        ra.addFlashAttribute("user", user);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/user/showUser");
        return mv;
    }
}
