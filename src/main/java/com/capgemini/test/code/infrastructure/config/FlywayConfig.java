package com.capgemini.test.code.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * FlywayConfig - Configuración de Flyway para ambos datasources
 *
 * WriteDB: Flyway automático de Spring Boot
 * ReadDB: Configuración manual en este bean
 */
@Configuration
public class FlywayConfig {

    /**
     * Flyway para ReadDB (PostgreSQL)
     * Se ejecuta automáticamente al iniciar la aplicación
     */
    @Bean
    public Flyway readdbFlyway(@Qualifier("readdbDataSource") DataSource dataSource) {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration/readdb")
            .baselineOnMigrate(true)
            .load();

        flyway.migrate();
        return flyway;
    }
}

