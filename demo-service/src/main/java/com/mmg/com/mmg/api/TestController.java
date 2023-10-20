package com.mmg.com.mmg.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fan
 * @Date: 2023/10/20
 * @Description:
 */
@RequestMapping("/")
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "Hello~";
    }
}
