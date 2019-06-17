package com.quick.shopping.service;

import com.quick.shopping.pojo.PurchaseRecordPojo;

import java.util.List;

public interface PurchaseService {

    /**
     * 处理购买业务
     * @param userId 用户编号
     * @param productId 产品编号
     * @param quantity 购买数量
     * @return 成功or失败
     */
    boolean purchase(Long userId, Long productId, int quantity);

    //保存购买记录
    boolean dealRedisPurchase(List<PurchaseRecordPojo> prpList);
}
