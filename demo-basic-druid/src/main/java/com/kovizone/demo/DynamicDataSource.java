package com.kovizone.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.kovizone.demo.config.DynamicDataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源配置
 *
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Configuration
public class DynamicDataSource {

    private DynamicDataSourceConfig dynamicDataSourceConfig;

    public static List<DruidDataSource> dynamicDataSourceList;

    public static String primaryDataSourceName;

    public DynamicDataSource(DynamicDataSourceConfig dynamicDataSourceConfig) {
        this.dynamicDataSourceConfig = dynamicDataSourceConfig;
    }

    /**
     * 动态数据源
     *
     * @return 动态数据源
     */
    @Bean
    @Primary
    public RoutingDataSource primaryDataSource() {
        dynamicDataSourceList = dynamicDataSourceConfig.getDatasources();
        Map<Object, Object> dataSourceMap = new LinkedHashMap<>(dynamicDataSourceList.size());
        for (DruidDataSource druidDataSource : dynamicDataSourceList) {
            dataSourceMap.put(druidDataSource.getName(), druidDataSource);
        }

        DruidDataSource primaryDataSource = dynamicDataSourceConfig.getDatasources().get(0);
        primaryDataSourceName = primaryDataSource.getName();
        return new RoutingDataSource(primaryDataSource, dataSourceMap);
    }

    public DynamicDataSourceConfig getDynamicDataSourceConfig() {
        return dynamicDataSourceConfig;
    }

    public void setDynamicDataSourceConfig(DynamicDataSourceConfig dynamicDataSourceConfig) {
        this.dynamicDataSourceConfig = dynamicDataSourceConfig;
    }
}
