package com.capgemini.test.code.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * WriteDB JPA Repositories Configuration
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb",
    entityManagerFactoryRef = "writedbEntityManagerFactory",
    transactionManagerRef = "writedbTransactionManager"
)
public class WriteDBRepositoriesConfig {
}

