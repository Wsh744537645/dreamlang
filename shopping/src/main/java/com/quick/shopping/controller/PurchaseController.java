package com.quick.shopping.controller;

import com.quick.shopping.service.PurchaseService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService = null;

    // 定义 JSP 视图
    @GetMapping("/test")
    public ModelAndView testPage() {
        ModelAndView mv = new ModelAndView("test");
        return mv;
    }

    @PostMapping("/purchase")
    public Result purchase(Long userId, Long productId, Integer quantity) {
        boolean success= purchaseService.purchase(userId, productId, quantity);
        String message = success? "抢购成功" : "抢购失败";
        Result result = new Result(success, message);
        return result;
    }

    // 响应结果
    @Data
    class Result {
        private boolean success = false;
        private String message = null;

        public Result() {
        }

        public Result(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

}
