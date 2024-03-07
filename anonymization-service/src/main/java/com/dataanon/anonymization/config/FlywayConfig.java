package com.dataanon.anonymization.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flywayStaging(@Qualifier("stagingDataSource") DataSource stagingDs) {
        return Flyway.configure()
                .dataSource(stagingDs)
                .schemas("staging")
                .locations("classpath:db/migration/staging")
                .baselineOnMigrate(true)
                .load();
    }

    @Bean(initMethod = "migrate")
    @DependsOn("flywayStaging")
    public Flyway flywayAnonymized(@Qualifier("anonymizedDataSource") DataSource anonymizedDs) {
        return Flyway.configure()
                .dataSource(anonymizedDs)
                .schemas("anonymized")
                .locations("classpath:db/migration/anonymized")
                .baselineOnMigrate(true)
                .load();
    }
}
