package com.kovizone.demo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@EnableDubbo(scanBasePackages = "com.kovizone.demo.service")
@EnableDiscoveryClient
@SpringBootApplication
public class DubboTemplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboTemplateApplication.class, args);
    }
}
