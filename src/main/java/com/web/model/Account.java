package com.web.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * User Account model, on top of the basic user data elements, 
 * each user has its capabilitiesList that will enable API call of restricted actions (like user look up) 
 *
 */
public class Account {
	private final String emailAddress;
	private String password;
	private boolean addressVerified = false;
	private String name = "";
	private String address = "";
	private List<Capability> capabilitiesList = new ArrayList<Capability>();
	
	public Account(String emailAddress, String password, boolean addressVerified, String name, String address,
			List<Capability> capabilitiesList) {
		this.emailAddress = emailAddress;
		this.password = password;
		this.addressVerified = addressVerified;
		this.name = name;
		this.address = address;
		this.capabilitiesList = capabilitiesList;
	}
	
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAddressVerified() {
		return addressVerified;
	}
	public void setAddressVerified(boolean addressVerified) {
		this.addressVerified = addressVerified;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<Capability> getCapabilitiesList() {
		return capabilitiesList;
	}
	public void setCapabilitiesList(List<Capability> capabilitiesList) {
		this.capabilitiesList = capabilitiesList;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	
}
