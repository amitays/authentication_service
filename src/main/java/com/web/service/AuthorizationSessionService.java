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

/**
 * authorization session management BL, 
 * Holds and manage the LogedIn users session for all users and machines
 *
 */

@Component
public class AuthorizationSessionService {
	private static List<AuthorizationSession> sessions = new ArrayList<AuthorizationSession>();
	private static JdkIdGenerator tokenGenerator = new JdkIdGenerator();
	
	/**
	 * Find the session entry with the given accountId and machineId
	 * @return true if a session could be found
	 * @throws IllegalArgumentException missing or empty input parameters
	 */
	public boolean isAuthorizationSessionExist(String accountId, String machineId) throws IllegalArgumentException{
		if(StringUtils.isEmpty(accountId) || StringUtils.isEmpty(machineId) ) throw new IllegalArgumentException();
		
		for(AuthorizationSession session : sessions) {
			if(session.getAccountId().equals(accountId) && session.getMachineId().equals(machineId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Generate and store AuthorizationSession for the given account and machine
	 * @param accountId
	 * @param machineId
	 * @return the newly created session token
	 * @throws AuthorizationSessionAllreadyExistException the user account already has a session on the given machine
	 */
	public String createAuthorizationSession(String accountId, String machineId) throws AuthorizationSessionAllreadyExistException {
		if(isAuthorizationSessionExist(accountId, machineId)) throw new AuthorizationSessionAllreadyExistException(accountId,machineId);
		
		final String token = tokenGenerator.generateId().toString();
		
		AuthorizationSession session = new AuthorizationSession(accountId, machineId, token, new Date());
		sessions.add(session);
		
		return token;		
	}
	
	/**
	 * Get an existing AuthorizationSession according to the session token
	 * @param token
	 * @return the stored AuthorizationSession
	 * @throws IllegalArgumentException IllegalArgumentException missing or empty input parameters
	 */
	private AuthorizationSession getAuthorizationSession(String token) throws IllegalArgumentException {
		if(StringUtils.isEmpty(token)) throw new IllegalArgumentException();
		
		for(AuthorizationSession session : sessions) {
			if(session.getToken().equals(token)) {
				return session;
			}
		}
		return null;
	}
	
	/**Decide whether the AuthorizationSessionValid, basic implementation, 
	 * just see that the token exist, and owned by the correct user and machine, ignore the timestamp for now
	 * @param token
	 * @param accountId
	 * @param machineId
	 * @return true if the AuthorizationSession is found and valid
	 */
	public boolean isAuthorizationSessionValid(String token, String accountId, String machineId) {
		AuthorizationSession session = getAuthorizationSession(token);
		return((session != null) && 
				(session.getAccountId().equals(accountId)) && 
				(session.getMachineId().equals(machineId)));		
	}
	
	/**
	 * Remove the AuthorizationSession from the session list
	 * @param token
	 * @return true if succeed
	 */
	public boolean removeAuthorizationSession(String token){
		return sessions.remove(getAuthorizationSession(token));
	}

	/**
	 * Find the AuthorizationSession according to the session token and return the users accountId
	 * @param sessionToken
	 * @return user accountId
	 * @throws AuthorizationSessionNotExistException cannot find the given token in the session list
	 */
	public String getAccountIdFromToken(String sessionToken) throws AuthorizationSessionNotExistException {
		final AuthorizationSession session = getAuthorizationSession(sessionToken);
		if(session == null) throw new AuthorizationSessionNotExistException(sessionToken);
		
		return session.getAccountId();
	}
	
	/**
	 * Clear current session list	
	 */
	public void reset() {
		sessions.clear();
	}
}
