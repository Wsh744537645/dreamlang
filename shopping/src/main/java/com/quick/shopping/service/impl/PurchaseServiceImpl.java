package com.quick.shopping.service.impl;

import com.quick.shopping.dao.ProductDao;
import com.quick.shopping.dao.PurchaseRecordDao;
import com.quick.shopping.pojo.ProductPojo;
import com.quick.shopping.pojo.PurchaseRecordPojo;
import com.quick.shopping.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private ProductDao productDao = null;
    @Autowired
    private PurchaseRecordDao purchaseRecordDao = null;

    @Autowired
    private StringRedisTemplate redisTemplate = null;

    String purchaseScript =
            // 先将产品编号保存到集合中
            " redis.call('sadd', KEYS[1], ARGV[2]) \n"
                    // 购买列表
                    + "local productPurchaseList = KEYS[2]..ARGV[2] \n"
                    // 用户编号
                    + "local userId = ARGV[1] \n"
                    // 产品键
                    + "local product = 'product_'..ARGV[2] \n"
                    // 购买数量
                    + "local quantity = tonumber(ARGV[3]) \n"
                    // 当前库存
                    + "local stock = tonumber(redis.call('hget', product, 'stock')) \n"
                    // 价格
                    + "local price = tonumber(redis.call('hget', product, 'price')) \n"
                    // 购买时间
                    + "local purchase_date = ARGV[4] \n"
                    // 库存不足，返回0
                    + "if stock < quantity then return 0 end \n"
                    // 减库存
                    + "stock = stock - quantity \n"
                    + "redis.call('hset', product, 'stock', tostring(stock)) \n"
                    // 计算价格
                    + "local sum = price * quantity \n"
                    // 合并购买记录数据
                    + "local purchaseRecord = userId..','..quantity..','"
                    + "..sum..','..price..','..purchase_date \n"
                    //将购买记录保存到list里
                    + "redis.call('rpush', productPurchaseList, purchaseRecord) \n"
                    // 返回成功
                    + "return 1 \n";
    // Redis 购买记录集合前缀
    private static final String PURCHASE_PRODUCT_LIST = "purchase_list_";
    // 抢购商品集合
    private static final String PRODUCT_SCHEDULE_SET = "product_schedule_set";
    // 32位 SHA1 编码，第一次执行的时候先让 Redis 进行缓存脚本返回
    private String sha1 = null;

    @Override
    public boolean purchase(Long userId, Long productId, int quantity){
        //购买时间
        Long purchaseDate = System.currentTimeMillis();
        Jedis jedis = null;
        try {
            jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
            //如果没有加载过，则先将脚本加载到Redis服务器，让其返回sha1
            if(sha1 == null){
                sha1 = jedis.scriptLoad(purchaseScript);
            }

            //执行脚本返回结果
            Object res = jedis.evalsha(sha1, 2, PRODUCT_SCHEDULE_SET, PURCHASE_PRODUCT_LIST, userId +"", productId + "", quantity +"", purchaseDate + "");
            Long result = (Long)res;
            return result == 1;
        }finally {
            if(jedis != null && jedis.isConnected()){
                jedis.close();
            }
        }
    }

//    @Override
//    @Transactional
//    public boolean purchase(Long userId, Long productId, int quantity) {
//        long startTime = System.currentTimeMillis();
//        while (true) {
//            long endTime = System.currentTimeMillis();
//            // 如果循环时间大于100 ms 返回终止循环
//            if(endTime - startTime > 1000){
//                return false;
//            }
//
//            //获取产品
//            ProductPojo productPojo = productDao.getProduct(productId);
//            //比较库存和商品数量
//            if (quantity > productPojo.getStock()) {
//                log.error("【购买商品】库存不足，quantity={}，productPojo={}", quantity, productPojo);
//                //throw new ProductException(ProductEnum.PRODUCT_NOT_ENOUGH);
//                return false;
//            }
//
//            //版本号
//            int version = productPojo.getVersion();
//            //扣库存
//            int result = productDao.decreaseProduct(productId, quantity, version);
//            // 如果更新数据失败，说明数据在多线程中被其他线程修改，导致失败返回
//            // 导致失败，则通过循环重入尝试购买商品
//            if (result == 0) {
//                log.error("【购买商品】更新库存失败，version={}", version);
//                continue;
//            }
//
//            // 插入购买记录
//            purchaseRecordDao.insertPurchaseRecord(initPurchaseRecord(userId, productPojo, quantity));
//
//            return true;
//        }
//    }

    //保存购买记录
    @Override
    // 当运行方法启用新的独立事务运行
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean dealRedisPurchase(List<PurchaseRecordPojo> prpList) {
        for(PurchaseRecordPojo pojo : prpList){
            purchaseRecordDao.insertPurchaseRecord(pojo);
            productDao.decreaseProduct(pojo.getProductId(), pojo.getQuantity());
        }
        return true;
    }

    // 初始化购买信息
    private PurchaseRecordPojo initPurchaseRecord(Long userId, ProductPojo product, int quantity){
        PurchaseRecordPojo purchaseRecordPojo = new PurchaseRecordPojo();
        purchaseRecordPojo.setUserId(userId);
        purchaseRecordPojo.setNote("购买日志，时间：" + System.currentTimeMillis());
        purchaseRecordPojo.setPrice(product.getPrice());
        purchaseRecordPojo.setProductId(product.getId());
        purchaseRecordPojo.setQuantity(quantity);
        double sum = product.getPrice() * quantity;
        purchaseRecordPojo.setSum(sum);

        return purchaseRecordPojo;
    }
}
