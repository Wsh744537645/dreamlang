//package com.springboot.mybatisdemo.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//
///**
// * Security测试
// */
//
//@Component
//public class SecurityTest extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private DataSource dataSource;
//    private AuthenticationManagerBuilder auth;
//
//    // 注入配置的阴匙
//    @Value("${system.user.password.secret}")
//    private String secret = null;
//
//    // 使用用户名称查询密码
//    String pwdQuery = " select user_name, pwd, available"
//            + " from t_user where user_name = ?";
//    // 使用用户名称查询角色信息
//    String roleQuery = " select u.user_name, r.role_name "
//            + " from t_user u, t_user_role ur, t_role r "
//            + "where u.id = ur.user_id and r.id = ur.role_id"
//            + " and u.user_name = ?";
//
//    /**
//     * 覆盖 WebSecurityConfigurerAdapter 用户详情方法
//     * @param auth 用户签名管理器构造器
//     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        this.auth = auth;
//        useRAMUserInfo();
//
//        //useDBUserInfo();
//    }
//
//    /**
//     * 使用内存签名服务
//     * 使用内存用户
//     */
//    private void useRAMUserInfo() throws Exception{
//        //密码编辑器
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder()
//
//
//                ;
//        //使用内存存储
//        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> userConfig=
//            auth.inMemoryAuthentication()
//                //设置密码编辑器
//                .passwordEncoder(passwordEncoder);
//
//        //注册用户 admin，密码为 abc，并赋予 USER 和 AMDIN 角色
//        userConfig.withUser("admin")
//                .password(passwordEncoder.encode("abc"))
//                .authorities("ROLE_USER", "USER_ADMIN");
//
//        // 注册用户myuser，密码为123456,并赋予USER的角色权限
//        userConfig.withUser("myuser")
//                // 可通过passwordEncoder.encode("123456")得到加密后的密码
//                .password(passwordEncoder.encode("123456"))
//                .authorities("ROLE_USER");
//    }
//
//    /**
//     * 覆盖 WebSecurityConfigurerAdapter 用户详情方法
//     * 使用数据库定义用户认证服务
//     */
//    private void useDBUserInfo() throws Exception{
//        // 密码编码器
//        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder(secret);
//        auth.jdbcAuthentication()
//                // 密码编码器
//                .passwordEncoder(passwordEncoder)
//                // 数据源
//                .dataSource(dataSource)
//                // 查询用户，自动判断密码是否一致
//                .usersByUsernameQuery(pwdQuery)
//                // 赋予权限
//                .authoritiesByUsernameQuery(roleQuery);
//    }
//}
