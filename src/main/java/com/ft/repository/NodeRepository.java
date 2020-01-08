package com.ft.repository;

import com.ft.domain.Node;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Node entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NodeRepository extends ElasticsearchCrudRepository<Node, String>, ElasticsearchRepository<Node, String> {

}
