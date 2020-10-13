package com.kovizone.demo.filter;

import com.kovizone.demo.util.FilterUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Slf4j
public class HeaderFilter implements Filter {

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
            Enumeration<String> enumeration = request.getHeaderNames();
            if (enumeration != null) {
                StringBuilder stringBuilder = new StringBuilder();
                while (enumeration.hasMoreElements()) {
                    stringBuilder.append("; ");
                    String name = enumeration.nextElement();
                    stringBuilder.append(name);
                    Object value = request.getHeader(name);
                    stringBuilder.append("=");
                    stringBuilder.append(value);
                }
                if (stringBuilder.length() > 0) {
                    log.debug("Header: " + stringBuilder.substring(1));
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
