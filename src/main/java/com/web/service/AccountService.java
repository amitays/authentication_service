package com.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.web.model.Capability;
import com.web.model.Account;
import com.web.service.Exceptions.IllegalAccountValueException;
import com.web.service.Exceptions.AccountAllreadyExistException;
import com.web.service.Exceptions.AccountNotExistException;

/**
 * Basic user account management BL, 
 * Holds and manage the user accounts list and facilitate the user credential check
 *
 */
@Component
public class AccountService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static List<Account> accounts;

	@Autowired(required = true)
	private void setAccounts(List<Account> accounts) {
		AccountService.accounts = accounts;
	}
	
	public final Account getAccount(final String emailAddress){
		for (Account account : accounts) {
			if (account.getEmailAddress().equals(emailAddress)) {
				return account;
			}
		}
		return null;
	}
	
	public boolean isAccountExist(final String emailAddress){
		return getAccount(emailAddress) != null;
	}
	
	/**
	 * Add a new user account after validating input parameters and the uniqueness of the Email address 
	 * @param emailAddress
	 * @param password plain text password
	 * @param capabilities user role/access level according to the CapabilityService model
	 * @return true if the user account was created successfully
	 * @throws IllegalAccountValueException missing or empty input parameters
	 * @throws AccountAllreadyExistException an account with the same Email address already exist
	 */
	public final Account addNewAccount(final String emailAddress, final String password, List<Capability> capabilities) throws IllegalAccountValueException, AccountAllreadyExistException{
		if(StringUtils.isEmpty(emailAddress) || StringUtils.isEmpty(password) || CollectionUtils.isEmpty(capabilities))
			throw new IllegalAccountValueException(emailAddress, password, capabilities);
		if(isAccountExist(emailAddress))
				throw new AccountAllreadyExistException(emailAddress);
		//in a real-life scenario, the password string will be hashed
		final Account account = new Account(emailAddress, password, false, "", "", capabilities);
		
		if(accounts.add(account)) return account;
				
		return null;
	}
	
	/**
	 * Compare the given password to the stored user account one  
	 * @param emailAddress
	 * @param password plain text password
	 * @return true if succeed
	 * @throws AccountNotExistException can not find the user account with the given Email address 
	 */
	public boolean authenticateAccount(final String emailAddress, final String password) throws AccountNotExistException {
		final Account account = getAccount(emailAddress);
		if(account == null) throw new AccountNotExistException(emailAddress);
		logger.debug(String.format("authenticateAccount: %s with password: %s", emailAddress, password));
		
		//since we do not hash the password string, we use simple string comparison
		return account.getPassword().equals(password);
	}
	
}
