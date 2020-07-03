package com.imooc.miaosha.controller;


import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @ResponseBody
    @GetMapping("/test1")
    public Result<String> test1() {
        return Result.success("hello, imooc");
    }

    @ResponseBody
    @GetMapping("/test2")
    public Result<String> test2() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @GetMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "hkx");
        
        return "hello";
    }

    // 测试查询
    @GetMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    // 测试事务
    @GetMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> tx() {

        userService.tx();
        return Result.success(true);
    }


    @GetMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {

        User u1 = redisService.get(UserKey.getById, "" + 1, User.class);
        return Result.success(u1);
    }
    @GetMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("hkx");
        Boolean v1 = redisService.set(UserKey.getById, "" + 1, user);
        return Result.success(v1);
    }

}
