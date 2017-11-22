package com.web.service.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class CapabilityNotFoundException extends Exception {
	public CapabilityNotFoundException(String name) {
		super("could not find Capability '" + name + "'.");
	}
}
