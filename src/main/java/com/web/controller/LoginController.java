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

/**
 * LoginController, expose the authorization session management capabilities, LogIn and LogOut users, 
 * Managing the session token distribution (self implemented OAuth capability)
 * Uses the spring source RESTful Web-Service annotations to initialize and invoke the different API calls
 *
 */
@RestController
public class LoginController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AuthorizationSessionService sessionService;
	
	/**
	 * LogIn the user to the current machine
	 * @param emailAddress
	 * @param password
	 * @param machineId naive machine identifier for this exercise purpose, we use the User-Agent as the identifier 
	 * @return a session token to be used in future API calls as a proof for the LogedIn status 
	 * @throws InvalidUsernameOrPassword user name and password do not match
	 * @throws AuthorizationSessionAllreadyExistException the user is already LogedIn on this machine
	 */
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
	
	/**
	 * LogOut the user, will work only if the user is LogedIn to this machine
	 * @param sessionToken given by the LogIn call
	 * @param emailAddress
	 * @param machineId naive machine identifier for this exercise purpose, we use the User-Agent as the identifier
	 * @return true if the LogOut was successful
	 * @throws AuthorizationSessionNotExistException the current user is not LogedIn to this machine
	 */
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
