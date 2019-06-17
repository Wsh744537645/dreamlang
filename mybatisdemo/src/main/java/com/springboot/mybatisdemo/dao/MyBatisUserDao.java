package com.springboot.mybatisdemo.dao;

import com.springboot.mybatisdemo.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyBatisUserDao {
    User getUser(Long id);

    int insertUser(User user);

    int updateUser(User user);

    // 查询用户，指定MyBatis的参数名称
    List<User> findUsers(@Param("userName") String userName, @Param("note") String note);

    int deleteUser(Long id);
}
