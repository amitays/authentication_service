package com.web.model;

import java.util.Date;


public class AuthorizationSession {
	private final String accountId;
	private final String machineId;
	private final String token;
	private final Date creationDate;
	
	public AuthorizationSession(String accountId, String machineId, String token, Date creationDate) {
		super();
		this.accountId = accountId;
		this.machineId = machineId;
		this.token = token;
		this.creationDate = creationDate;
	}
	
	public String getAccountId() {
		return accountId;
	}
	public String getMachineId() {
		return machineId;
	}
	public String getToken() {
		return token;
	}
	public Date getCreationDate() {
		return creationDate;
	}
}
