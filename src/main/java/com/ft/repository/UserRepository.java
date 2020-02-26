package com.ft.repository;

import com.ft.domain.QUser;
import com.ft.domain.User;
import com.querydsl.core.BooleanBuilder;

import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.Instant;

/**
 * Spring Data MongoDB repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>, QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {

	/**
	 * Override Querydsl handling on predicate
	 */
	@Override
	default void customize(QuerydslBindings bindings, QUser root) {
		bindings.bind(root.email).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.login).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.firstName).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.lastName).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		// Predicate builder explicit for SET
		bindings.bind(root.authorities).all((path, values) -> {
			BooleanBuilder builder = new BooleanBuilder();
			values.stream().filter(x -> !x.isEmpty())
                    .flatMap(x -> x.stream())
                    .distinct()
                    .collect(Collectors.toSet())
                    .iterator().forEachRemaining(item -> builder.or(path.any().name.equalsIgnoreCase(item.getName())));
			return Optional.of(builder.getValue());
		});
	}

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneByEmailIgnoreCase(String email);

    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneByLogin(String login);

    Page<User> findAllByLoginNot(Pageable pageable, String login);
}
