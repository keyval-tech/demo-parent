package com.kovizone.demo;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@EnableOpenApi
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);

        // 修改流控规则
        List<FlowRule> flowRuleList = new ArrayList<>();
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("/hello");
        flowRule.setLimitApp("default");
        // grade：限流阈值类型
        // 可选值：
        // 0 - 代表根据并发数量来限流
        // 1 - 代表根据QPS来进行流量控制
        flowRule.setGrade(0);
        // count：限流阈值
        flowRule.setCount(5);
        // controlBehavior：流控效果
        // 可选值：
        // 0 - 默认，直接拒绝
        // 1 - Warm Up（让通过的流量缓慢增加）
        // 2 - 匀速排队
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
        flowRule.setStrategy(RuleConstant.STRATEGY_CHAIN);
    }
}
