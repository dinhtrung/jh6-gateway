package com.ft.repository;

import com.ft.domain.Node;
import com.ft.domain.QNode;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Node entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NodeRepository extends MongoRepository<Node, String>, QuerydslPredicateExecutor<Node>, QuerydslBinderCustomizer<QNode> {

	/**
	 * Override Querydsl handling on predicate
	 */
	@Override
	default void customize(QuerydslBindings bindings, QNode root) {
		// All attribute of QNode should be here
		bindings.bind(root.id).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.name).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.state).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.type).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.meta).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.fields).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
	}
}
