package com.quick.shopping.pojo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 购买记录
 */

@Data
@Alias("purchaseRecord")
public class PurchaseRecordPojo implements Serializable {
    private static final long serialVersionUID = 867929833648263104L;

    private Long id;
    private Long userId;
    private Long productId;
    private double price;
    private int quantity;
    private double sum;
    private Timestamp purchaseTime;
    private String note;
}
