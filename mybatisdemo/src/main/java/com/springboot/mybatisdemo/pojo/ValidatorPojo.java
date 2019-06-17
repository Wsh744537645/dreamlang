package com.springboot.mybatisdemo.pojo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class ValidatorPojo {

    Thread thread;
    //非空判断
    @NotNull(message = "id 不能为空")
    Long id;

    @Future(message = "需要一个将来的时间")// 只能是将来的日期
    // @Past // 只能是过去的日期
    @DateTimeFormat(pattern = "yyy-MM-dd")// 日期格式化转换
    @NotNull
    private Date date;

    @NotNull
    @DecimalMin(value = "0.1") //最小值0.1元
    @DecimalMax(value = "10000.00") //最大值10000元
    private Double doubleValue = null;

    @NotNull
    @Min(value = 1, message = "最小值为1")
    @Max(value = 88, message = "最大值为88")
    private Integer integer;

    @Range(min = 1, max = 888, message = "范围1至888")
    private Long range;

    //邮箱验证
    @Email(message = "邮箱格式错误")
    private String email;

    @Size(min = 20, max = 30, message = "字符串长度要求20-30之间")
    private String size;
}
