package com.web.model;

public class Capability {
	private final int level;
	private final String name;
	
	public Capability(int level, String name) {
		super();
		this.level = level;
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public String getName() {
		return name;
	}
}
