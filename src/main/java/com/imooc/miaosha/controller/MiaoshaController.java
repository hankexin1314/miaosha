package com.imooc.miaosha.controller;

import com.imooc.miaosha.access.AccessLimit;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.redis.*;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
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

    private final Map<Long, Boolean> localOverMap = new HashMap<>();


    @GetMapping(value="/reset")
    @ResponseBody
    public Result<Boolean> reset() {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for(GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getMiaoshaOrderByUidGid);
        redisService.delete(MiaoshaKey.isGoodsOver);
        miaoshaService.reset(goodsList);
        return Result.success(true);
    }

    @PostMapping(value="/{path}/do_miaosha")
    @ResponseBody
    public Result<Integer> miaosha(MiaoshaUser user,
                                   @RequestParam("goodsId")long goodsId,
                                   @PathVariable("path") String path) {
        // 判断是否登录
        if(user == null)
            return Result.error(CodeMsg.SESSION_ERROR);
        // 验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        boolean over = localOverMap.get(goodsId);
        if(over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        // 减库存 返回值是减少后的值
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if(stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
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


    @AccessLimit(seconds=5, maxCount=5, needLogin=true) // 5秒内最多访问5次，需要登录
    @GetMapping(value="/path")
    @ResponseBody
    public Result<String> getMiaoshaPath(MiaoshaUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value="verifyCode", defaultValue="0")int verifyCode) {

        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    @GetMapping(value="/verifyCode")
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, MiaoshaUser user,
                                              @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image  = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
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
