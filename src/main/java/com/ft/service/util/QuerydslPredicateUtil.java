package com.ft.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

public class QuerydslPredicateUtil {


	public static List<Predicate> toPredicates(String root, Map<String, Object> map) {
		List<Predicate> predicates = new ArrayList<>();

		for (Entry<String, Object> entry : map.entrySet()) {
			if (!entry.getKey().startsWith("meta")) continue;
			String path = root != null ? root + '.' + entry.getKey() : entry.getKey();
			Object value = entry.getValue();
			predicates.add(new SimplePath(path).eq(value.toString()));
		}
		return predicates;
	}

	public static class SimplePath extends StringPath {

		private static final long serialVersionUID = 1L;

		protected SimplePath(String var) {
			super(var);
		}
	}
}
