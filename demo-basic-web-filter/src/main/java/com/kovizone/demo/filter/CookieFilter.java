package com.kovizone.demo.filter;

import com.kovizone.demo.util.FilterUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Slf4j
public class CookieFilter implements Filter {

    private final static boolean LOG_SWITCH = true;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (FilterUtil.isStaticUri(request.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (LOG_SWITCH) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();
                    String value = cookie.getValue();
                    stringBuilder.append("; ");
                    stringBuilder.append(name);
                    stringBuilder.append("=");
                    stringBuilder.append(value);
                }
                if (stringBuilder.length() > 0) {
                    log.debug("Cookie: " + stringBuilder.substring(1));
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
