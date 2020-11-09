package com.kovizone.demo.controller;

import com.kovizone.demo.endpoint.WebSocketEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Controller
@Slf4j
public class MyController {
    /**
     * 页面请求
     *
     * @param sid sid
     * @return mav
     */
    @GetMapping("/socket/view/{sid}")
    public ModelAndView socket(@PathVariable String sid) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("sid", sid);
        return mav;
    }

    /**
     * 推送数据接口
     *
     * @param sid     sid
     * @param message 消息
     * @return 发送结果
     */
    @ResponseBody
    @RequestMapping("/socket/push")
    public String pushToWeb(@RequestParam(value = "sid", required = false) String sid, @RequestParam("message") String message) {
        try {
            WebSocketEndpoint.sendInfo(message, sid);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return "推送失败";
        }
        return "发送成功";
    }
}