/**
 * String 转换成User类的转换器，实现接口Converter后Springboot会自动扫描到WebDataBinder
 */

package com.springboot.mybatisdemo.converter;

import com.springboot.mybatisdemo.pojo.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUserConverter implements Converter<String, User> {
    @Override
    public User convert(String source) {
        User user = new User();
        String[] strArr = source.split("-");
        Long id = Long.parseLong(strArr[0]);
        String userName = strArr[1];
        String note = strArr[2];
        user.setId(id);
        user.setUserName(userName);
        user.setNote(note);
        return user;
    }
}
