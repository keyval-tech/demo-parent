package com.kovizone.demo.controller;

import com.kovizone.demo.service.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@RestController
public class GatewayController {

    @DubboReference(version = "1.0.0", group = "test", check = false, timeout = 60)
    private HelloService helloService;

    @GetMapping("/hello")
    public String hello(@RequestParam("name") String name) {

        return helloService.hello(name);
    }

}
