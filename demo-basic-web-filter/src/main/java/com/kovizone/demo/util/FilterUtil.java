package com.kovizone.demo.util;

import cn.hutool.core.util.StrUtil;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
public class FilterUtil {

    /**
     * 静态资源类型
     */
    private final static List<String> STATIC_RESOURCES_TYPE_LIST = new ArrayList<>();

    static {
        STATIC_RESOURCES_TYPE_LIST.add(".html");
        STATIC_RESOURCES_TYPE_LIST.add(".htm");
        STATIC_RESOURCES_TYPE_LIST.add(".css");
        STATIC_RESOURCES_TYPE_LIST.add(".js");
        STATIC_RESOURCES_TYPE_LIST.add(".jpg");
        STATIC_RESOURCES_TYPE_LIST.add(".png");
        STATIC_RESOURCES_TYPE_LIST.add(".ico");
        STATIC_RESOURCES_TYPE_LIST.add(".gif");
        STATIC_RESOURCES_TYPE_LIST.add(".woff2");
    }

    /**
     * 判断Http请求是否为AJAX请求
     *
     * @param request Http请求
     * @return 是否为AJAX请求
     */
    public static boolean isAjax(ServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
    }

    public static boolean isStaticUri(String uri) {
        String type;
        if (uri.contains(".")) {
            type = uri.substring(uri.lastIndexOf(".") + 1);
            if (type.contains("?")) {
                type = type.substring(0, type.indexOf("?"));
            }
            if (type.contains(";")) {
                type = type.substring(0, type.indexOf(";"));
            }
            type = type.trim();
        } else {
            type = null;
        }
        return isStaticType(type);
    }

    public static boolean isStaticType(String type) {
        if (type != null && !type.startsWith(".")) {
            type = "." + type;
        }
        return StrUtil.isNotEmpty(type) && STATIC_RESOURCES_TYPE_LIST.contains(type);
    }
}
