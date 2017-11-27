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
		
	/**
	 * get the capability according to its capability name
	 * @param name
	 * @return the found capability object
	 * @throws CapabilityNotFoundException cannot fined the capability with the given name
	 */
	public Capability getCapabilityByName(final String name) throws CapabilityNotFoundException{
		for (Capability capability : capabilityList) {
			if (capability.getName().equals(name)) {
				return capability;
			}
		}
		throw new CapabilityNotFoundException(name);
	}
	
	/**
	 * Check if the action is permitted using a naive classification model.
	 * User capability level value should be higher than the resource capability level
	 * @param minimalCapabilities resource minimal capability level
	 * @param existingCapabilities user's capability level
	 * @return true if the action is permitted
	 */
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
