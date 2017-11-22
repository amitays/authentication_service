package com.web.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.web.model.Capability;
import com.web.model.Account;
/**
 * Basic config, mainly to mitigate the lack of data persistency,
 * add default users to the account service and basic Capabilities for the authorization service usage
 *
 */
@Configuration
public class AppConfig {

	static List<Capability> capabilityList = new ArrayList<Capability>();
	static List<Account> accounts = new ArrayList<Account>();
	
	static {
		
		capabilityList.add(new Capability(100, "Administrator"));
		capabilityList.add(new Capability(50, "ListUsers"));
		capabilityList.add(new Capability(1, "User"));
		
		Account user1 = new Account("amitays@msn.com", "password", true, 
				"Amitay Svetlit","San Diego, CA", 
				Arrays.asList(capabilityList.get(0)));
		
		Account user2 = new Account("user@domain.com", "void", true, 
				"John Doe","LA, CA", 
				Arrays.asList(capabilityList.get(2)));
		
		accounts.add(user1);
		accounts.add(user2);
	}

	@Bean
	public List<Account> getUsrList(){
		return AppConfig.accounts;
	}
	
	@Bean
	public List<Capability> getCapability(){
		return capabilityList;
	}
	
}
