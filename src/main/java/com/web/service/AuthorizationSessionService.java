package com.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.JdkIdGenerator;
import org.springframework.util.StringUtils;

import com.web.model.AuthorizationSession;
import com.web.service.Exceptions.AuthorizationSessionAllreadyExistException;
import com.web.service.Exceptions.AuthorizationSessionNotExistException;

@Component
public class AuthorizationSessionService {
	private static List<AuthorizationSession> sessions = new ArrayList<AuthorizationSession>();
	private static JdkIdGenerator tokenGenerator = new JdkIdGenerator();
	
	public boolean isAuthorizationSessionExist(String accountId, String machineId) {
		if(StringUtils.isEmpty(accountId) || StringUtils.isEmpty(machineId) ) throw new IllegalArgumentException();
		
		for(AuthorizationSession session : sessions) {
			if(session.getAccountId().equals(accountId) && session.getMachineId().equals(machineId)) {
				return true;
			}
		}
		return false;
	}
	
	public String createAuthorizationSession(String accountId, String machineId) throws AuthorizationSessionAllreadyExistException {
		if(isAuthorizationSessionExist(accountId, machineId)) throw new AuthorizationSessionAllreadyExistException(accountId,machineId);
		
		final String token = tokenGenerator.generateId().toString();
		
		AuthorizationSession session = new AuthorizationSession(accountId, machineId, token, new Date());
		sessions.add(session);
		
		return token;		
	}
	
	private AuthorizationSession getAuthorizationSession(String token) {
		if(StringUtils.isEmpty(token)) throw new IllegalArgumentException();
		
		for(AuthorizationSession session : sessions) {
			if(session.getToken().equals(token)) {
				return session;
			}
		}
		return null;
	}
	
	/**basic implementation, just see that the token exist, and owned by the correct user and machine,
	 * ignore the timestamp for now
	 * @param token
	 * @param accountId
	 * @param machineId
	 * @return
	 */
	public boolean isAuthorizationSessionValid(String token, String accountId, String machineId) {
		AuthorizationSession session = getAuthorizationSession(token);
		return((session != null) && 
				(session.getAccountId().equals(accountId)) && 
				(session.getMachineId().equals(machineId)));		
	}
	
	public boolean removeAuthorizationSession(String token){
		return sessions.remove(getAuthorizationSession(token));
	}

	public String getAccountIdFromToken(String sessionToken) throws AuthorizationSessionNotExistException {
		final AuthorizationSession session = getAuthorizationSession(sessionToken);
		if(session == null) throw new AuthorizationSessionNotExistException(sessionToken);
		
		return session.getAccountId();
	}
	
	public void reset() {
		sessions.clear();
	}
}
