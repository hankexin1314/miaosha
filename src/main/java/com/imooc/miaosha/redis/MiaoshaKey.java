package com.imooc.miaosha.redis;

public class MiaoshaKey extends BasePrefix{

    private MiaoshaKey(int expireSeconds, String prefix) { // 避免被实例化
        super(prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey(0, "go");
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "mp");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "vc");
}
