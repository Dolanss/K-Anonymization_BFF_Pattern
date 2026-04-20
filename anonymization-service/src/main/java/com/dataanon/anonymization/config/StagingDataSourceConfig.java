package com.dataanon.anonymization.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.dataanon.anonymization.repository.staging",
    entityManagerFactoryRef = "stagingEntityManagerFactory",
    transactionManagerRef = "stagingTransactionManager"
)
public class StagingDataSourceConfig {

    @Primary
    @Bean("stagingDataSource")
    @ConfigurationProperties(prefix = "staging.datasource")
    public DataSource stagingDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean("stagingEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean stagingEntityManagerFactory(
            @Qualifier("stagingDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.dataanon.anonymization.domain.staging");
        factory.setPersistenceUnitName("staging");
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(adapter);
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "validate");
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.default_schema", "staging");
        factory.setJpaPropertyMap(props);
        return factory;
    }

    @Primary
    @Bean("stagingTransactionManager")
    public PlatformTransactionManager stagingTransactionManager(
            @Qualifier("stagingEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JpaTransactionManager(factory.getObject());
    }
}
