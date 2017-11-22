package com.web.service.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class AccountAllreadyExistException extends Exception {
	public AccountAllreadyExistException(String name) {
		super("Account named: '" + name + "' Allready Exist.");
	}
}
