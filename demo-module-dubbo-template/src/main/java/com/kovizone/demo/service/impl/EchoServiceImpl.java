package com.kovizone.demo.service.impl;

import com.kovizone.demo.service.EchoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Slf4j
@DubboService(version = "1.0.0", group = "test")
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String arg) {
        log.info(arg);
        return arg;
    }
}
