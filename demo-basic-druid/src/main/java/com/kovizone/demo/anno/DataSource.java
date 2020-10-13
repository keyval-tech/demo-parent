package com.kovizone.demo.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 选择数据源
 *
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

    /**
     * 指定数据源名
     *
     * <p>优先级低于{@code index}</p>
     *
     * @return 数据源名
     */
    String value() default "";

    /**
     * 指定数据源配置中的指定索引对应的数据源
     *
     * <p>优先级大于{@code value}</p>
     *
     * @return 数据源索引
     */
    int index() default -1;
}
