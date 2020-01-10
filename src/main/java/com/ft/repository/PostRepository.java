package com.ft.repository;

import com.ft.domain.Post;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Post entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostRepository extends ElasticsearchCrudRepository<Post, String>, ElasticsearchRepository<Post, String> {

}
