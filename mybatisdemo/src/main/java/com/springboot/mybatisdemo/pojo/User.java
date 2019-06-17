package com.springboot.mybatisdemo.pojo;

import com.springboot.mybatisdemo.enumeration.SexEnum;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias("user")
@Data
public class User implements Serializable {
    private Long id = null;

    private String userName = null;

    private String note = null;

    // 性别枚举，这里需要使用 typeHandler 进行转换
    private SexEnum sex = null;
}
