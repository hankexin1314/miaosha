package com.imooc.miaosha.vo;

import com.imooc.miaosha.domain.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVo {

    private GoodsVo goods;
    private OrderInfo order;
}
