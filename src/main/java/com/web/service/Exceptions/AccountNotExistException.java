package com.web.service.Exceptions;

public class AccountNotExistException extends Exception {
	public AccountNotExistException(String name) {
		super("Account named: '" + name + "' Do not Exist.");
	}
}
