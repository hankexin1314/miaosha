package com.imooc.miaosha.redis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class BasePrefix implements KeyPrefix{

    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    @Override
    public int expireSeconds() { // 默认0代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName(); // 保证不重复
        return className + ":" + prefix;
    }
}
