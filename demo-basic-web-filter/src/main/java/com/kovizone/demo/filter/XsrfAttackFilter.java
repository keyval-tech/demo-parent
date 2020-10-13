package com.kovizone.demo.filter;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import com.kovizone.demo.util.FilterUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 在执行特定请求时，在请求参数中必须含有会话ID的HASH，以防XSRF攻击
 *
 * <p>在{@code static}中维护需要检测的请求的范围</p>
 *
 * <p>在{@code hash}方法中指定hash算法</p>
 *
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Slf4j
public class XsrfAttackFilter implements Filter {

    private String sessionIdHashKeyName;

    private String hashAlgorithm;

    public XsrfAttackFilter(String sessionIdHashKeyName) {
        this.sessionIdHashKeyName = sessionIdHashKeyName;
        this.hashAlgorithm = "MD5";
    }

    public XsrfAttackFilter(String sessionIdHashKeyName, String hashAlgorithm) {
        this.sessionIdHashKeyName = sessionIdHashKeyName;
        this.hashAlgorithm = hashAlgorithm;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (FilterUtil.isStaticUri(request.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String sessionId = request.getSession(true).getId();
        String uri = request.getRequestURI();

        // 预防XSRF|CSRF
        String sessionIdHash = request.getParameter(sessionIdHashKeyName);
        boolean flag = false;
        if (StrUtil.isEmpty(sessionIdHash)) {
            log.warn("疑似XSRF攻击，HASH为空，uri: " + uri);
            flag = true;
        } else if (sessionId == null) {
            log.warn("疑似XSRF攻击，Session ID为空，uri: " + uri);
            flag = true;
        } else if (!hash(sessionId).equalsIgnoreCase(sessionIdHash)) {
            log.warn("疑似XSRF攻击，对比失败，uri: " + uri);
            flag = true;
        }
        if (flag) {
            response.setStatus(403);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 指定Hash算法
     *
     * @param arg 原文
     * @return hash文
     */
    private String hash(String arg) {
        switch (hashAlgorithm) {
            case "MD5":
                return SecureUtil.md5(arg);
            case "SM3":
                return SmUtil.sm3(arg);
            default:
                return RandomUtil.randomString(32);
        }
    }
}
