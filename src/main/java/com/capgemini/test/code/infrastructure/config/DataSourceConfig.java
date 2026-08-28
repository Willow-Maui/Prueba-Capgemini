package com.capgemini.test.code.infrastructure.config;

import jakarta.persistence.EntityManagerFactory;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * DataSourceConfig - Configuración de múltiples datasources
 *
 * WriteDB: PostgreSQL (Source of Truth - la conexión previa)
 * ReadDB: MySQL (Eventual Consistent Replica - nueva)
 *
 * Cada datasource tiene su propio EntityManager y TransactionManager
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    // ==================== WRITEDB (PostgreSQL - Previa, sin cambios) ====================

    @Bean(name = "writedbDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.writedb")
    public DataSource writedbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "writedbEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean writedbEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(writedbDataSource());
        em.setPackagesToScan(
            "com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb"
        );
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        var props = em.getJpaPropertyMap();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.format_sql", true);
        props.put("hibernate.use_sql_comments", true);

        return em;
    }

    @Bean(name = "writedbTransactionManager")
    @Primary
    public PlatformTransactionManager writedbTransactionManager(
            EntityManagerFactory writedbEntityManagerFactory) {
        return new JpaTransactionManager(writedbEntityManagerFactory);
    }

    // ==================== READDB (MySQL - Nueva) ====================

    @Bean(name = "readdbDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.readdb")
    public DataSource readdbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "readdbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean readdbEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(readdbDataSource());
        em.setPackagesToScan(
            "com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb"
        );
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        var props = em.getJpaPropertyMap();
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.put("hibernate.format_sql", true);
        props.put("hibernate.use_sql_comments", true);

        return em;
    }

    @Bean(name = "readdbTransactionManager")
    public PlatformTransactionManager readdbTransactionManager(
            EntityManagerFactory readdbEntityManagerFactory) {
        return new JpaTransactionManager(readdbEntityManagerFactory);
    }
}


