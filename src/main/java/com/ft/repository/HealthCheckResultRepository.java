package com.ft.repository;

import com.ft.domain.HealthCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data MongoDB repository for the {@link HealthCheck} entity.
 */
public interface HealthCheckRepository extends MongoRepository<HealthCheck, String> {

    Page<HealthCheck> findAllByCreatedAtBetween(Instant fromDate, Instant toDate, Pageable pageable);

    List<HealthCheck> findByCreatedAtBefore(Instant before);
}
