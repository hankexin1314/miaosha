package com.imooc.miaosha.controller;


import com.imooc.miaosha.dao.IGoodsDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_list")
    public String toList(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user,
                         @PathVariable("goodsId")Long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int remainSeconds = 0;
        int miaoshaStatus = 0;
        if(now < startAt) { // 秒杀尚未开始 倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now) / 1000);
        }
        else if(now > endAt) { // 秒杀哦结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }
        else {
            miaoshaStatus = 1;
            remainSeconds = 0;

        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        return "goods_detail";
    }


}
