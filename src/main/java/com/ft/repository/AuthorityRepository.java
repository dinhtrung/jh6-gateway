package com.ft.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ft.domain.Authority;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends ElasticsearchCrudRepository<Authority, String>, ElasticsearchRepository<Authority, String> {

}
