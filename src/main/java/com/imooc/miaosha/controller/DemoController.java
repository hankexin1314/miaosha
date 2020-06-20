package com.imooc.miaosha.controller;


import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {


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
}
