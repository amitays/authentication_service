package com.web.service.Exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.web.model.Capability;

@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY)
public class IllegalAccountValueException extends Exception {
	public IllegalAccountValueException(final String emailAddress, final String password, List<Capability> capabilities) {
		super(String.format("Canot create account with illegal parameters: emailAddress-%s, password-%s %s",
				emailAddress, password, CollectionUtils.isEmpty(capabilities)? "or empty capabilities" : ""));
	}
}
