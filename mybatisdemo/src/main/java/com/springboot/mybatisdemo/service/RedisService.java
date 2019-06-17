package com.springboot.mybatisdemo.service;

public interface RedisService {
    String getValue(String key);

    int setValue(String key, String value);

    void testStringAndHash();

    void testList();

    void testSet();

    void testZset();

    void testMulti();
}
