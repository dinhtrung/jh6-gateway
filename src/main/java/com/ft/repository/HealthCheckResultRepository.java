package com.ft.repository;

import com.ft.domain.HealthCheckResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data MongoDB repository for the {@link HealthCheckResult} entity.
 */
public interface HealthCheckResultRepository extends MongoRepository<HealthCheckResult, String> {

    Page<HealthCheckResult> findAllByCreatedAtBetween(Instant fromDate, Instant toDate, Pageable pageable);

    List<HealthCheckResult> findByCreatedAtBefore(Instant before);
}
