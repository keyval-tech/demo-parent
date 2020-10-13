package com.kovizone.demo.aspect;

import cn.hutool.core.util.StrUtil;
import com.kovizone.demo.DynamicDataSource;
import com.kovizone.demo.RoutingDataSource;
import com.kovizone.demo.anno.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * DataSource注解切面
 *
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
public class DynamicDataSourceAspect {

    @Before("@annotation(com.kovizone.demo.anno.DataSource) || @within(com.kovizone.demo.anno.DataSource)")
    public void beforeSwitchDataSource(JoinPoint point) {
        Class<?> clazz = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Class<?>[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();

        try {
            Method method = clazz.getMethod(methodName, argClass);

            int dataSourceIndex = -1;
            if (method.isAnnotationPresent(DataSource.class)) {
                dataSourceIndex = method.getAnnotation(DataSource.class).index();
            } else if (clazz.isAnnotationPresent(DataSource.class)) {
                dataSourceIndex = clazz.getAnnotation(DataSource.class).index();
            }

            if (dataSourceIndex > -1) {
                RoutingDataSource.setDataSource(DynamicDataSource.dynamicDataSourceList.get(dataSourceIndex));
            } else {
                String dataSourceName = null;
                if (method.isAnnotationPresent(DataSource.class)) {
                    dataSourceName = method.getAnnotation(DataSource.class).value();
                } else if (clazz.isAnnotationPresent(DataSource.class)) {
                    dataSourceName = clazz.getAnnotation(DataSource.class).value();
                }
                RoutingDataSource.setDataSource(StrUtil.emptyToDefault(dataSourceName, DynamicDataSource.primaryDataSourceName));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @After("@annotation(com.kovizone.demo.anno.DataSource) || @within(com.kovizone.demo.anno.DataSource)")
    public void afterSwitchDataSource() {
        RoutingDataSource.resetDataSource();
    }
}
