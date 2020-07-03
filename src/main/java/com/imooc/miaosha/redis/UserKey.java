package com.imooc.miaosha.redis;

public class UserKey extends BasePrefix {

    private UserKey(String prefix) { // 避免被实例化
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
