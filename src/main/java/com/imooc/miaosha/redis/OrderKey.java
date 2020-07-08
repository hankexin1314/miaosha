package com.imooc.miaosha.redis;

public class OrderKey extends BasePrefix {

    private OrderKey(String prefix) { // 避免被实例化
        super(prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
