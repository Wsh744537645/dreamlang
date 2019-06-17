package com.quick.shopping.resultenum;

import lombok.Data;

public enum ProductEnum {
    PRODUCT_NOT_ENOUGH(10, "商品数量不足");

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ProductEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
