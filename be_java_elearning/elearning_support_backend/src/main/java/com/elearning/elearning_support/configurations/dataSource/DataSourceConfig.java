package com.elearning.elearning_support.configurations.dataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import com.elearning.elearning_support.enums.dataSource.DataSourceRouteEnum;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {
    private final String MASTER_DATASOURCE_PREFIX_PROP = "spring.master.datasource";

    private final String SLAVE_DATASOURCE_PREFIX_PROP = "spring.slave.datasource";

    private final Environment environment;

    @Bean
    @Primary
    public DataSource dataSource() {
        final RoutingDataSource routingDataSource = new RoutingDataSource();

        // create data sources
        final DataSource masterDataSource = buildDataSourceInstance(MASTER_DATASOURCE_PREFIX_PROP, Boolean.FALSE);
        final DataSource slaveDataSource = buildDataSourceInstance(SLAVE_DATASOURCE_PREFIX_PROP, Boolean.TRUE);

        // create targetDataSources map <type, instance>
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceRouteEnum.MASTER, masterDataSource);
        targetDataSources.put(DataSourceRouteEnum.SLAVE, slaveDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    /**
     * build a data source instance
     */
    private DataSource buildDataSourceInstance(String dataSourcePrefix, Boolean isReadOnly) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName(environment.getProperty(String.format("%s.hikari.pool-name", dataSourcePrefix)));
        hikariConfig.setSchema(environment.getProperty(String.format("%s.hikari.schema", dataSourcePrefix)));
        hikariConfig.setDriverClassName(environment.getProperty(String.format("%s.driverClassName", dataSourcePrefix)));
        hikariConfig.setJdbcUrl(environment.getProperty(String.format("%s.url", dataSourcePrefix)));
        hikariConfig.setUsername(environment.getProperty(String.format("%s.username", dataSourcePrefix)));
        hikariConfig.setPassword(environment.getProperty(String.format("%s.password", dataSourcePrefix)));
        hikariConfig.setReadOnly(isReadOnly);
        return new HikariDataSource(hikariConfig);
    }
}
