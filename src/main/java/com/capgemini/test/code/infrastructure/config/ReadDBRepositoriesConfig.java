package com.capgemini.test.code.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * ReadDB JPA Repositories Configuration
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb",
    entityManagerFactoryRef = "readdbEntityManagerFactory",
    transactionManagerRef = "readdbTransactionManager"
)
public class ReadDBRepositoriesConfig {
}

