package com.kovizone.demo;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * 路由数据源
 *
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final static ThreadLocal<String> DATASOURCE_NAME = new ThreadLocal<>();

    public RoutingDataSource(DruidDataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        log.info("Default datasource: " + defaultTargetDataSource.getName());
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        StringBuilder s = new StringBuilder();
        targetDataSources.forEach((key, value) -> {
            s.append(",");
            s.append(key);
        });
        log.info("Target datasources: " + s.substring(1));
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    public static void setDataSource(DruidDataSource druidDataSource) {
        DATASOURCE_NAME.set(druidDataSource.getName());
    }

    public static void setDataSource(String key) {
        log.info("set datasource: " + key);
        DATASOURCE_NAME.set(key);
    }

    public static String getDataSourceName() {
        return DATASOURCE_NAME.get();
    }

    public static void resetDataSource() {
        log.info("reset datasource");
        DATASOURCE_NAME.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DATASOURCE_NAME.get();
    }
}
