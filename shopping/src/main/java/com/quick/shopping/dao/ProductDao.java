package com.quick.shopping.dao;

import com.quick.shopping.pojo.ProductPojo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao {
    //获取产品
    ProductPojo getProduct(Long id);

    // 减库存，而@Param 标明 MyBatis 参数传递给后台
    int decreaseProduct(@Param("id") Long id, @Param("quantity") int quantity);
}
