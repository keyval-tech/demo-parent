package com.kovizone.demo.filter;

import com.kovizone.demo.util.FilterUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 日志过滤器
 *
 * @author KoviChen
 */
@Slf4j
public class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        boolean isAjax = FilterUtil.isAjax(request);
        String ajaxFlag = isAjax ? "[AJAX]" : "";
        String sessionId = request.getSession(true).getId();

        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (method.length() < 4) {
            method = " " + method;
        }

        String baseLog = String.format("%s : %s -> %s %s", sessionId, method, uri, ajaxFlag);

        if (FilterUtil.isStaticUri(uri)) {
            filterChain.doFilter(servletRequest, servletResponse);
            log.debug(baseLog);
            return;
        }

        log.info(baseLog);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
