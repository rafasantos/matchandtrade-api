package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.UserRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.UserJson;

@Component
public class UserValidator {

	@Autowired
	private UserRepository userRepository;

	/**
	 * {@code UserJson.email} cannot change on PUT operations.
	 * @param userId
	 * @param json
	 */
	public void validatePut(UserJson json) {
		UserEntity userEntity = userRepository.get(json.getUserId());
		if (json.getEmail() == null || userEntity == null || !json.getEmail().equalsIgnoreCase(userEntity.getEmail())) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Cannot update User.email on PUT operations.");
		}
	}
	
	public void validateGetById(AuthenticationEntity authenticationEntity, Integer userId) {
		if (!authenticationEntity.getUser().getUserId().equals(userId)) {
			throw new AuthorizationException(AuthorizationException.Type.FORBIDDEN);
		}
	}
}
