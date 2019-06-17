package com.quick.shopping.dao;

import com.quick.shopping.pojo.PurchaseRecordPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRecordDao {
    int insertPurchaseRecord(PurchaseRecordPojo pr);
}
