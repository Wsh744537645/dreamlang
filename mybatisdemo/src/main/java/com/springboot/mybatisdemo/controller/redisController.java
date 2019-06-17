package com.springboot.mybatisdemo.controller;

import com.springboot.mybatisdemo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class redisController {

    @Autowired
    RedisService redisService = null;

    @Autowired
    RedisTemplate redisTemplate = null;
    @Autowired
    StringRedisTemplate stringRedisTemplate = null;

    @RequestMapping("/getValue")
    public String getValue(String key){
        return redisService.getValue(key);
    }

    @RequestMapping("/setValue")
    public String setValue(String key, String value){
        return "success: " + redisService.setValue(key, value);
    }

    @RequestMapping("/stringAndHash")
    public Map<String, Object> testStringAndHash() {
        redisService.testStringAndHash();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        return map;
    }

    //使用 Redis 流水线测试性能
    @RequestMapping("/pipeline")
    public Map<String, Object> testPipeline(){
        Long start = System.currentTimeMillis();
        List list = redisTemplate.executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                for(int i = 1; i <= 100000; i++){
                    redisOperations.opsForValue().set("pipeline_" + i, "value_" + i, 30, TimeUnit.SECONDS);
                    if(i == 100000){
                        String value = (String) redisOperations.opsForValue().get("pipeline" + i);
                        System.out.println("命令只是进入队列，所以值为空【" + value +"】");
                    }
                }
                return null;
            }
        });

        Long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) + "毫秒。");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        return map;
    }

    //测试Redis消息订阅，在这里发布消息，控制台打印出渠道和消息
    @RequestMapping("/publish")
    public Map<String, Object> publishRedisMessage(String channel, String message){
        redisTemplate.convertAndSend(channel, message);//Redis客户端发布消息指令: publish topic1 msg

        Map<String, Object> result = new HashMap<>();
        result.put("channel", channel);
        result.put("message", message);
        return result;
    }

    //测试lua使用，不带参数
    @RequestMapping("/lua")
    public Map<String, Object> testLua(){
        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<String>();
        //设置脚本
        defaultRedisScript.setScriptText("return 'Hello Redis'");
        //定义返回类型。注意：如果没这个定义，Spring不会返回结果
        defaultRedisScript.setResultType(String.class);
        RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();

        //执行lua脚本
        String str = (String)redisTemplate.execute(defaultRedisScript, stringRedisSerializer, stringRedisSerializer, null);

        Map<String, Object> map = new HashMap<>();
        map.put("str", str);
        return map;
    }

    //测试使用lua脚本，带参数
    @RequestMapping("lua2")
    public Map<String, Object> testLua2(String key1, String key2, String value1, String value2){
        //定义lua脚本
        String lua = "redis.call('set', KEYS[1], ARGV[1]) \n"
                + "redis.call('set', KEYS[2], ARGV[2]) \n"
                + "local str1 = redis.call('get', KEYS[1]) \n"
                + "local str2 = redis.call('get', KEYS[2]) \n"
                + "if str1 == str2 then  \n"
                + "return 1 \n"
                + "end \n"
                + "return 0 \n";
        System.out.println(lua);
        //结果返回为Long
        DefaultRedisScript<Long> longDefaultRedisScript = new DefaultRedisScript<>();
        longDefaultRedisScript.setScriptText(lua);
        longDefaultRedisScript.setResultType(Long.class);

        //采用字符串序列化串
        RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
        //定义key参数
        List<String> keyList = new ArrayList<>();
        keyList.add(key1);
        keyList.add(key2);

        Long result = (Long) redisTemplate.execute(longDefaultRedisScript, stringRedisSerializer, stringRedisSerializer, keyList, value1, value2);
        Map<String, Object> map = new HashMap<>();
        map.put("result", result);
        return map;
    }
}
