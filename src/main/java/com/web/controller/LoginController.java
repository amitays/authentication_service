package com.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.service.AccountService;
import com.web.service.AuthorizationSessionService;
import com.web.service.Exceptions.AccountNotExistException;
import com.web.service.Exceptions.AuthorizationSessionAllreadyExistException;
import com.web.service.Exceptions.AuthorizationSessionNotExistException;
import com.web.service.Exceptions.InvalidUsernameOrPassword;

@RestController
public class LoginController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AuthorizationSessionService sessionService;
		
	@PostMapping("/logInService/logIn")
	public String logIn(@RequestParam(value="emailAddress") String emailAddress, 
			@RequestParam(value="password")String password, @RequestHeader("User-Agent") String machineId) 
					throws InvalidUsernameOrPassword, AuthorizationSessionAllreadyExistException{
		logger.debug(String.format("logIn: %s with password: %s on: %s", emailAddress, password, machineId));
		
		try {
			if(accountService.authenticateAccount(emailAddress, password)) {
				return sessionService.createAuthorizationSession(emailAddress, machineId);
			}
		} catch (AccountNotExistException e) {
			throw new InvalidUsernameOrPassword();
		} 
		throw new InvalidUsernameOrPassword();
	}
	
	@PostMapping("/logInService/logOut")
	public boolean logOut(@RequestParam(value="sessionToken") String sessionToken, 
			@RequestParam(value="emailAddress") String emailAddress,
			@RequestHeader("User-Agent") String machineId) throws AuthorizationSessionNotExistException{
		logger.debug(String.format("logOut: %s with token %s on: %s", emailAddress, sessionToken, machineId));
		if(sessionService.isAuthorizationSessionValid(sessionToken, emailAddress, machineId)){
			return sessionService.removeAuthorizationSession(sessionToken);
		}
		throw new AuthorizationSessionNotExistException(emailAddress, machineId);
		
	}
	
}
