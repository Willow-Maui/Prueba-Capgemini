package com.capgemini.test.code.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;

/**
 * FlywayConfig - Configuración segura de Flyway para WriteDB
 *
 * Nota: Spring Boot autoconfigure está deshabilitado porque tenemos datasources personalizados.
 * Este bean maneja las migrations manualmente para WriteDB (PostgreSQL).
 *
 * ReadDB (MySQL) no necesita Flyway - se sincroniza desde WriteDB.
 */
@Configuration
public class WriteDbFlywayConfig {

    private final DataSource writedbDataSource;

    public WriteDbFlywayConfig(
            @Qualifier("writedbDataSource")
            DataSource writedbDataSource) {
        this.writedbDataSource = writedbDataSource;
    }

    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {

        Flyway flyway = Flyway.configure()
                .dataSource(writedbDataSource)
                .locations("classpath:db/migration/writedb")
                .baselineOnMigrate(true)
                .encoding("UTF-8")
                .load();

        flyway.migrate();
    }
}


