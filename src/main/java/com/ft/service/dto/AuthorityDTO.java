package com.ft.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthorityDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private String name;
    
	private Map<String, Object> meta = new HashMap<>();
    
	private Set<String> sites = new HashSet<>();
    
	private Set<String> permissions = new HashSet<>();
	
	@Override
	public String toString() {
		return "AuthorityDTO [name=" + name + ", meta=" + meta + ", sites=" + sites + ", permissions=" + permissions
				+ "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}

	public Set<String> getSites() {
		return sites;
	}

	public void setSites(Set<String> sites) {
		this.sites = sites;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}
	
	public AuthorityDTO addPermission(String permission) {
		this.permissions.add(permission);
		return this;
	}
	
	public AuthorityDTO addSite(String site) {
		this.sites.add(site);
		return this;
	}
	
	public AuthorityDTO addMeta(String key, Object value) {
		this.meta.put(key, value);
		return this;
	}
	

}
