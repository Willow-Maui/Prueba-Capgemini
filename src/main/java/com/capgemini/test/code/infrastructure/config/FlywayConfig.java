package com.capgemini.test.code.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
public class FlywayConfig {

    private final DataSource writedbDataSource;

    public FlywayConfig(@Qualifier("writedbDataSource") DataSource writedbDataSource) {
        this.writedbDataSource = writedbDataSource;
    }

    /**
     * Ejecuta las migrations de Flyway al iniciar la aplicación
     * Se ejecuta después de que todos los beans estén inicializados
     */
    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        try {
            Flyway flyway = Flyway.configure()
                .dataSource(writedbDataSource)
                .locations("classpath:db/migration/writedb")
                .baselineOnMigrate(true)
                .load();

            flyway.migrate();
        } catch (Exception e) {
            // Log pero no falla si las migrations fallan
            System.err.println("Error executing Flyway migrations: " + e.getMessage());
        }
    }
}

