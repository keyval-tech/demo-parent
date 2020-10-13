package com.kovizone.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 动态数据源配置
 *
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring")
public class DynamicDataSourceConfig {

    private List<DruidDataSource> datasources;

    public List<DruidDataSource> getDatasources() {
        return datasources;
    }

    public void setDatasources(List<DruidDataSource> datasources) {
        this.datasources = datasources;
    }
}
