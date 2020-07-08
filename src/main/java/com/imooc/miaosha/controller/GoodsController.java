package com.imooc.miaosha.controller;


import com.imooc.miaosha.dao.IGoodsDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(Model model, HttpServletRequest request, HttpServletResponse response) {
        // 从缓存中取页面
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        // 手动渲染
        WebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model,MiaoshaUser user,
                                        @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }

//    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
//    @ResponseBody
//    public String detail2(Model model, MiaoshaUser user,
//                         @PathVariable("goodsId")Long goodsId,
//                         HttpServletRequest request, HttpServletResponse response) {
//        // 从缓存中取页面
//        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
//        if(!StringUtils.isEmpty(html)) {
//            return html;
//        }
//        model.addAttribute("user", user);
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        model.addAttribute("goods", goods);
//        long startAt = goods.getStartDate().getTime();
//        long endAt = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//        int remainSeconds = 0;
//        int miaoshaStatus = 0;
//        if(now < startAt) { // 秒杀尚未开始 倒计时
//            miaoshaStatus = 0;
//            remainSeconds = (int)((startAt - now) / 1000);
//        }
//        else if(now > endAt) { // 秒杀哦结束
//            miaoshaStatus = 2;
//            remainSeconds = -1;
//        }
//        else {
//            miaoshaStatus = 1;
//            remainSeconds = 0;
//
//        }
//        model.addAttribute("remainSeconds", remainSeconds);
//        model.addAttribute("miaoshaStatus", miaoshaStatus);
//
//        // 手动渲染
//        WebContext ctx = new WebContext(request, response,
//                request.getServletContext(), request.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
//        if(!StringUtils.isEmpty(html)) {
//            redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
//        }
//        return html;
//    }


}
