package com.springboot.mybatisdemo.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;

@Configuration
public class RedisMessageTaskRegistConfig {

    //redis连接工厂
    @Autowired
    private RedisConnectionFactory connectionFactory = null;

    //redis消息监听器
    @Autowired
    private MessageListener messageListener = null;

    //任务池
    private ThreadPoolTaskScheduler taskScheduler = null;

    //创建任务池，运行线程等待处理Redis的消息
    @Bean
    public ThreadPoolTaskScheduler initTaskScheduler(){
        if(taskScheduler != null){
            return taskScheduler;
        }

        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        return taskScheduler;
    }

    //定义Redis的监听容器
    @Bean
    public RedisMessageListenerContainer initRedisContainer(){
        RedisMessageListenerContainer redisContainer = new RedisMessageListenerContainer();
        redisContainer.setConnectionFactory(connectionFactory);
        //设置运行任务池
        redisContainer.setTaskExecutor(initTaskScheduler());
        //定义监听渠道，名称为topic1
        Topic topic = new ChannelTopic("topic1");
        //使用监听器监听Redis的消息
        redisContainer.addMessageListener(messageListener, topic);

        return redisContainer;
    }

    /** 自定义Redis缓存管理器
     * 也可以使用配置启用缓存管理器
     * 这里注释@Bean，使用配置启用
     * @return
     */
    //@Bean(name = "redisCacheManager" )
    public RedisCacheManager initRedisCacheManager() {
        // Redis加锁的写入器
        RedisCacheWriter writer= RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
        // 启动Redis缓存的默认设置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置JDK序列化器
        config = config.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer()));
        // 禁用前缀
        config = config.disableKeyPrefix();
        //设置10 min超时
        config = config.entryTtl(Duration.ofMinutes(10));
        // 创建缓Redis存管理器
        RedisCacheManager redisCacheManager = new RedisCacheManager(writer, config);
        return redisCacheManager;
    }
}
