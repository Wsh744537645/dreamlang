package com.springboot.mybatisdemo.service.impl;

import com.springboot.mybatisdemo.dao.MyBatisUserDao;
import com.springboot.mybatisdemo.pojo.User;
import com.springboot.mybatisdemo.service.MyBatisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyBatisUserServiceImpl implements MyBatisUserService {

    @Autowired
    MyBatisUserDao myBatisUserDao = null;

    /**
     * 获得指定用户信息(获取 id，取参数 id 缓存用户)
     * .@Cacheable 表示先从缓存中通过定义的键查询，如果可以查询到数据，则返回，否则执行该方法，返回数据，并且将返回结果保存到缓存中。
     * @param id
     * @return
     */
    @Override
    @Transactional
    @Cacheable(value = "redisCache", key = "'redis_user_'+#id")
    public User getUser(Long id) {
        return myBatisUserDao.getUser(id);
    }

    /**
     * 插入用户，最后 MyBatis 会回填 id，取结果 id 缓存用户
     * .@CachePut表示将方法结果返回存放到缓存中。
     * @param user
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, timeout = 1)
    @CachePut(value = "redisCache", key = "'redis_user_'+#result.id")
    public User insertUser(User user) {
        myBatisUserDao.insertUser(user);
        return user;
    }

    /**
     * 更新数据后，更新缓存，如果 condition 配置项使结果返回为 null，不缓存
     * @param id
     * @param userName
     * @return
     */
    @Override
    @Transactional
    @CachePut(value = "redisCache", condition = "#result != 'null'", key = "'redis_user_'+#id")
    public User updateUserName(Long id, String userName) {
        //此处调用getUserCache方法，该方法缓存注解失败
        //所以这里还会执行sql，将查询到数据库最新数据
        User user = this.getUser(id);
        if(user == null){
            return null;
        }
        user.setUserName(userName);
        myBatisUserDao.updateUser(user);
        return user;
    }

    //命中率低，所以不采用缓存机制
    @Override
    @Transactional
    public List<User> findUsers(String userName, String note) {
        return myBatisUserDao.findUsers(userName, note);
    }

    /**
     * 移除缓存
     * .@CacheEvict 通过定义的键移除缓存，它有一个 Boolean 类型的配置项 beforeInvocation，表示在方法之前或者之后移除缓存。因为其默认值为 false，所以默认为方法之后将缓存移除。
     * @param id
     * @return
     */
    @Override
    @Transactional
    @CacheEvict(value = "redisCache", key = "'redis_user_'+#id")
    public int deleteUser(Long id) {
        return myBatisUserDao.deleteUser(id);
    }
}
