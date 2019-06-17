package com.quick.shopping.pojo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * 产品
 */

@Data
@Alias("product")
public class ProductPojo implements Serializable {
    private static final long serialVersionUID = 1449798035693032543L;

    private Long id;
    private String productName;
    private int stock;
    private double price;
    private int version;
    private String note;
}
