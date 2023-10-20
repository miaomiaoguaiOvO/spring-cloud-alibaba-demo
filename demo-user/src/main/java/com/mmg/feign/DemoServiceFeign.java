package com.mmg.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: fan
 * @Date: 2023/10/20
 * @Description:
 */
@FeignClient(value = "demo-service")
public interface DemoServiceFeign {
    @GetMapping("/test")
    ResponseEntity<String> test();
}
