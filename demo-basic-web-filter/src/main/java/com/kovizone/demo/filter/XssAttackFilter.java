package com.kovizone.demo.filter;

import com.kovizone.demo.util.FilterUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 检测请求参数中是否存在脚本标签和脚本代码
 *
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Slf4j
public class XssAttackFilter implements Filter {

    public final static Pattern XSS_CHECK_PATTERN = Pattern.compile(".*(((?i)<(\\s)*[a-zA-Z]+)|((\\s)*(var|let)(\\s)+)|($(\\s)*\\()).*");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (FilterUtil.isStaticUri(request.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && parameterMap.size() != 0) {

            Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet) {

                String[] parameterValues = entry.getValue();
                if (parameterValues != null && parameterValues.length > 0) {
                    for (String parameterValue : parameterValues) {
                        if (XSS_CHECK_PATTERN.matcher(URLEncoder.encode(parameterValue, "UTF-8")).find()) {
                            response.setStatus(403);
                            log.warn("疑似XSS攻击");
                            return;
                        }
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
