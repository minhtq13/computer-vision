package com.elearning.elearning_support.configurations.dataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import com.elearning.elearning_support.enums.dataSource.DataSourceRouteEnum;
import com.elearning.elearning_support.utils.object.ObjectUtils;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DataSourceRouteEnum> routeContext = new ThreadLocal<>();

    /**
     * set data source route
     */
    public static void setRoute(DataSourceRouteEnum route){
        routeContext.set(route);
    }

    /**
     * clear data source route
     */
    public static void clearRoute(){
        routeContext.remove();
    }

    /**
     * get data source route
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return ObjectUtils.getOrDefault(routeContext.get(), DataSourceRouteEnum.MASTER);
    }

}
