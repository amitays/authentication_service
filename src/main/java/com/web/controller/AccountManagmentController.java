package com.web.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.model.Account;
import com.web.model.Capability;
import com.web.service.AccountService;
import com.web.service.AuthorizationSessionService;
import com.web.service.CapabilityService;
import com.web.service.Exceptions.AccountAllreadyExistException;
import com.web.service.Exceptions.ActionNotPermitedException;
import com.web.service.Exceptions.AuthorizationSessionNotExistException;
import com.web.service.Exceptions.CapabilityNotFoundException;
import com.web.service.Exceptions.IllegalAccountValueException;

@RestController
public class AccountManagmentController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AuthorizationSessionService sessionService;
	
	@Autowired
	private CapabilityService capabilityService;
	
	@PostMapping("/accountManagment/addNewUserAccount")
	public boolean addNewUserAccount(
			@RequestParam(value="emailAddress") String emailAddress, 
			@RequestParam(value="password")String password) throws AccountAllreadyExistException, IllegalAccountValueException{
		
		logger.debug(String.format("adding new user account: %s with password: %s", emailAddress, password));
		
		Account newAccount = null;
		try {
			newAccount = accountService.addNewAccount(emailAddress, password, Arrays.asList(capabilityService.getCapabilityByName("User")));
		} catch (CapabilityNotFoundException e) {
			logger.error("Cannot add user",e);
		}
		
		return newAccount != null;
	}
	
	@PostMapping("/accountManagment/getUserAccount")
	public Account getUserAccount(
			@RequestParam(value="emailAddress")  String emailAddress, 
			@RequestParam(value="sessionToken") String sessionToken) throws ActionNotPermitedException, AuthorizationSessionNotExistException{		
		String logedInAccountId = sessionService.getAccountIdFromToken(sessionToken);
		Account logedInAccount = accountService.getAccount(logedInAccountId);		
		Capability listUsers = null;
		try {
			listUsers = capabilityService.getCapabilityByName("ListUsers");
		} catch (CapabilityNotFoundException e) {
			logger.error("Cannot add user",e);
		}
		
		logger.debug(String.format("%s is getting $s user account", logedInAccount.getEmailAddress(), emailAddress));
		
		if(logedInAccount.getEmailAddress().equals(emailAddress) || 
				capabilityService.isActionPermited(Arrays.asList(listUsers), logedInAccount.getCapabilitiesList())){
			return accountService.getAccount(emailAddress);
		}
		
		throw new ActionNotPermitedException("ListUsers", logedInAccountId);
	}
}
