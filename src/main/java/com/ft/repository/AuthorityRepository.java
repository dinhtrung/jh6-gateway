package com.ft.repository;

import com.ft.domain.Authority;
import com.ft.domain.QAuthority;
import com.querydsl.core.BooleanBuilder;

import java.util.Optional;
import java.util.stream.Collectors;

import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String>, QuerydslPredicateExecutor<Authority>, QuerydslBinderCustomizer<QAuthority> {
	
	/**
	 * Override Querydsl handling on predicate
	 */
	@Override
	default void customize(QuerydslBindings bindings, QAuthority root) {
		bindings.bind(root.name).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.id).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		// Predicate builder explicit for SET
		bindings.bind(root.sites).all((path, values) -> {
			BooleanBuilder builder = new BooleanBuilder();
			values.stream().filter(x -> !x.isEmpty())
                    .flatMap(x -> x.stream())
                    .distinct()
                    .collect(Collectors.toSet())
                    .iterator().forEachRemaining(item -> builder.or(path.any().containsIgnoreCase(item)));
			return Optional.of(builder.getValue());
		});
		// Predicate builder explicit for Permissions
		bindings.bind(root.permissions).all((path, values) -> {
			BooleanBuilder builder = new BooleanBuilder();
			values.stream().filter(x -> !x.isEmpty())
                    .flatMap(x -> x.stream())
                    .distinct()
                    .collect(Collectors.toSet())
                    .iterator().forEachRemaining(item -> builder.or(path.any().containsIgnoreCase(item)));
			return Optional.of(builder.getValue());
		});
		bindings.bind(root.meta).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
	}
	
	
}
