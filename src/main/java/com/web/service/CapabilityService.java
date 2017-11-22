package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.web.model.Capability;
import com.web.service.Exceptions.CapabilityNotFoundException;

/**
 * basic classification management BL, 
 * uses a naive classification model that permit resources access to users that their capability level 
 * is higher that the resource capability level value
 *
 */
@Component
public class CapabilityService {
	private static List<Capability> capabilityList;

	@Autowired(required = true)
	private void setCapabilityList(List<Capability> capabilityList) {
		CapabilityService.capabilityList = capabilityList;
	}
		
	public Capability getCapabilityByName(final String name) throws CapabilityNotFoundException{
		for (Capability capability : capabilityList) {
			if (capability.getName().equals(name)) {
				return capability;
			}
		}
		throw new CapabilityNotFoundException(name);
	}
	
	public boolean isActionPermited(List<Capability> minimalCapabilities, List<Capability> existingCapabilities ){
		if( minimalCapabilities == null || existingCapabilities == null) throw new IllegalArgumentException();
		
		//no minimal capabilities, permit action 
		if(minimalCapabilities.isEmpty()) return true;
		
		//no minimal capabilities, do not permit action 
		if(existingCapabilities.isEmpty()) return false;
		
		//look for the highest existing capability level
		int maxExistingCapabilityLevel = 0;
		for(Capability capability : existingCapabilities) {
			if(maxExistingCapabilityLevel < capability.getLevel()) {
				maxExistingCapabilityLevel = capability.getLevel();
			}
		}
		
		//check that maxExistingCapabilityLevel it is higher than all minimal capability levels
		for(Capability capability : minimalCapabilities) {
			if(maxExistingCapabilityLevel < capability.getLevel()) {
				return false;
			}
		}
		
		return true;
	}
}
