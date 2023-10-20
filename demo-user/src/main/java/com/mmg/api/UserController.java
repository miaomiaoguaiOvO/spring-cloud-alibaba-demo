package com.mmg.api;

import com.mmg.feign.DemoServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fan
 * @Date: 2023/10/20
 * @Description:
 */
@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private DemoServiceFeign demoServiceFeign;
    @GetMapping("/user")
    public Object user(){
        return demoServiceFeign.test();
    }
}
