package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import com.imooc.miaosha.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;

    @Autowired
    private MiaoshaService miaoshaService;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(MiaoshaUser user,
                         @RequestParam("goodsId")long goodsId) {
        // 判断是否登录
        if(user == null)
            return Result.error(CodeMsg.SESSION_ERROR);

        localOverMap.get(goodsId);
        // 减库存 返回值是减少后的值
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if(stock < 0)
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        // 还有库存，入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        return Result.success(0);
    }

    /**
     * 秒杀成功返回订单id
     * 库存不足返回-1
     * 排队中返回0
     * @param user
     * @param goodsId
     * @return
     */
    @GetMapping(value = "/result")
    @ResponseBody
    public Result<Long> miaoshaResult(MiaoshaUser user,
                                @RequestParam("goodsId")long goodsId) {
        // 判断是否登录
        if(user == null)
            return Result.error(CodeMsg.SESSION_ERROR);
        long result = miaoshaService.getMiaoShaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null)
            return;
        for(GoodsVo goods: goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }
}
