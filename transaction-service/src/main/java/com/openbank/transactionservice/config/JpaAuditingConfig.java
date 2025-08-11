package com.openbank.transactionservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for JPA auditing.
 * Enables automatic population of audit fields in BaseEntity.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
