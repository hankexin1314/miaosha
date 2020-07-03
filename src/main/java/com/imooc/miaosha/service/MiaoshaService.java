package com.imooc.miaosha.service;


import com.imooc.miaosha.dao.IGoodsDao;
import com.imooc.miaosha.domain.Goods;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
        // 减库存，下订单，写入秒杀订单
        goodsService.reduceStock(goodsVo);
        OrderInfo orderInfo = orderService.createOrder(user, goodsVo);
        return orderInfo;
    }
}
