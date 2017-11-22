package com.web.service.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class ActionNotPermitedException extends Exception {
	public ActionNotPermitedException(String actionName, String accountId) {
		super("Action '" + actionName + "'not permited for user: '" + accountId + "'");
	}
}