package com.kovizone.demo.filter;

import com.alibaba.fastjson.JSONObject;
import com.kovizone.demo.util.FilterUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Slf4j
public class ParameterFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (FilterUtil.isStaticUri(request.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && parameterMap.size() != 0) {
            log.info("ParameterMap: " + JSONObject.toJSONString(parameterMap));
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
