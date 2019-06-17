package com.springboot.mybatisdemo.service.impl;

import com.springboot.mybatisdemo.service.AsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 使用多线程异步的service
 */

@Service
public class AsyncServiceImpl implements AsyncService {
    @Override
    @Async //声明使用异步调用
    public void generateReport() {
        // 打印异步线程名称
        System.out.println("报表线程名称: 【" + Thread.currentThread().getName() + "】");
    }
}
