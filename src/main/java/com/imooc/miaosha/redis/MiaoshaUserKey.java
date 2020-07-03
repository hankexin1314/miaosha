package com.imooc.miaosha.redis;

public class MiaoshaUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 2600 * 24 * 2;

    private MiaoshaUserKey(int expireSeconds, String prefix) { // 避免被实例化
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
}
