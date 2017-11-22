package com.web.service.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class InvalidUsernameOrPassword extends Exception {
	public InvalidUsernameOrPassword() {
		super("Invalid Username Or Password");
	}
}
