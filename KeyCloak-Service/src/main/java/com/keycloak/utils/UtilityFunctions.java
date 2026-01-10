package com.keycloak.utils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class UtilityFunctions {

	private UtilityFunctions() {
	}

	public static final Function<String, List<String>> roles = token -> {
		DecodedJWT jwt = JWT.decode(token);
		@SuppressWarnings("unchecked")
		List<String> allRoles = (List<String>) jwt.getClaim(Constants.REALM_ACCESS_CLAIM).asMap()
				.getOrDefault(Constants.ROLES_CLAIM, Collections.emptyList());
		return allRoles.stream().filter(role -> !Constants.ROLES_TO_SKIP.contains(role)).toList();
	};

	public static final <T> List<T> toListOrNull(T value) {
		return Optional.ofNullable(value).filter(v -> !(v instanceof String && ((String) v).isEmpty()))
				.map(Collections::singletonList).orElse(null);
	}
}
