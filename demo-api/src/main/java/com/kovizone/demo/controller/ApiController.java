package com.kovizone.demo.controller;

import com.kovizone.demo.anno.DataSource;
import com.kovizone.demo.mapper.SysUserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Api
@RefreshScope
@RestController
@DataSource(index = 0)
public class ApiController {

    @Resource
    private SysUserMapper sysUserMapper;

    @GetMapping("/hello")
    @ApiOperation(value = "你好", notes = "没啥用，测试swagger")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", paramType = "query", required = true, defaultValue = "张三")
    })
    public String hello(@RequestParam("name") String name) {
        sysUserMapper.selectList(null);
        return "Hi! " + name + "!";
    }
}
