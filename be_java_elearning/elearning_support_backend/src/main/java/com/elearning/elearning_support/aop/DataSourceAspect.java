package com.elearning.elearning_support.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.elearning.elearning_support.annotations.dataSource.DataSource;
import com.elearning.elearning_support.configurations.dataSource.RoutingDataSource;
import com.elearning.elearning_support.enums.dataSource.DataSourceRouteEnum;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Order(0)
@Slf4j(topic = "DataSourceAspect")
public class DataSourceAspect {

    @Around(value = "@annotation(dataSource)")
    public Object dataSourcePoint(ProceedingJoinPoint proceedingJoinPoint, DataSource dataSource) throws Throwable {
        RoutingDataSource.setRoute(dataSource.value());
        log.info("======= USING DATASOURCE [{}] =======", dataSource.value().name());
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            // re-route to MASTER
            RoutingDataSource.setRoute(DataSourceRouteEnum.MASTER);
            log.info("======= RE-ROUTING DATASOURCE TO [{}] =======", DataSourceRouteEnum.MASTER);
        }
    }
}
