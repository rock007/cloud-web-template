package org.cloud.webtemplate.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

	@Primary
	@Bean(name = "masterDataSource")
	@Qualifier("masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master") 
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }
	
	@Bean(name = "secondDataSource")
	@Qualifier("secondDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.second") 
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

}
