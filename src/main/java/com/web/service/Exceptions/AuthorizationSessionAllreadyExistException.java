package com.web.service.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class AuthorizationSessionAllreadyExistException extends Exception {
	
	public AuthorizationSessionAllreadyExistException(String accountId, String machineId) {
		super("Account named: '" + accountId + "' Already LogedIn on '" + machineId + "'");
	}
}
