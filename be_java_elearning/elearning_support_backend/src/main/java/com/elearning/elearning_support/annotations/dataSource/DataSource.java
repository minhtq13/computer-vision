package com.elearning.elearning_support.annotations.dataSource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.elearning.elearning_support.enums.dataSource.DataSourceRouteEnum;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface DataSource {
    DataSourceRouteEnum value() default DataSourceRouteEnum.MASTER;
}
