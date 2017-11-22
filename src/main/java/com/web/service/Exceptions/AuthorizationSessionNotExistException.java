package com.web.service.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class AuthorizationSessionNotExistException extends Exception {
	public AuthorizationSessionNotExistException(String accountId, String machineId) {
		super("Account named: '" + accountId + "' not LogedIn on '" + machineId + "'");
	}

	public AuthorizationSessionNotExistException(String token) {
		super("Authorization session not exist '" + token + "'");
	}
}
