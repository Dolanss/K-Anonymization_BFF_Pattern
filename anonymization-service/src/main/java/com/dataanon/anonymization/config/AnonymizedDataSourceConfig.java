package com.dataanon.anonymization.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    basePackages = "com.dataanon.anonymization.repository.anonymized",
    entityManagerFactoryRef = "anonymizedEntityManagerFactory",
    transactionManagerRef = "anonymizedTransactionManager"
)
public class AnonymizedDataSourceConfig {

    @Bean("anonymizedDataSource")
    @ConfigurationProperties(prefix = "anonymized.datasource")
    public DataSource anonymizedDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("anonymizedEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean anonymizedEntityManagerFactory(
            @Qualifier("anonymizedDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.dataanon.anonymization.domain.anonymized");
        factory.setPersistenceUnitName("anonymized");
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(adapter);
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "validate");
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.default_schema", "anonymized");
        factory.setJpaPropertyMap(props);
        return factory;
    }

    @Bean("anonymizedTransactionManager")
    public PlatformTransactionManager anonymizedTransactionManager(
            @Qualifier("anonymizedEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JpaTransactionManager(factory.getObject());
    }
}
