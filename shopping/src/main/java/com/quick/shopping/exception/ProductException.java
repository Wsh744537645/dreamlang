package com.quick.shopping.exception;

import com.quick.shopping.resultenum.ProductEnum;

public class ProductException extends RuntimeException {

    private Integer code;

    public ProductException(ProductEnum productEnum) {
        super(productEnum.getMessage());

        this.code = productEnum.getCode();
    }
}
