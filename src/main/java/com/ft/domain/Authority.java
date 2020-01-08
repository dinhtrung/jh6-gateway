package com.ft.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An authority (a security role) used by Spring Security.
 */
@Document(indexName = "jhi_authority")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    @Field(type = FieldType.Keyword)
    private String name;
    
    private Map<String, Object> meta = new HashMap<String, Object>();
    
    private Set<String> sites = new HashSet<String>();
    
    private Set<String> permissions = new HashSet<String>();
    
    @Override
	public String toString() {
		return "Authority [name=" + name + "]";
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Authority name(String name) {
    	this.name = name;
    	return this;
    }

	public String getId() {
		return name;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}
	
	public Authority meta(Map<String, Object> meta) {
		this.meta = meta;
		return this;
	}
	
	public Authority addMeta(String key, Object value) {
		this.meta.put(key, value);
		return this;
	}
	
	public Authority sites(Set<String> sites) {
		this.sites = sites;
		return this;
	}
	
	public Set<String> getSites() {
		return sites;
	}

	public void setSites(Set<String> sites) {
		this.sites = sites;
	}
	
	public Authority addSite(String site) {
		this.sites.add(site);
		return this;
	}
	
	public Authority permissions(Set<String> permissions) {
		this.permissions = permissions;
		return this;
	}
	
	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}
	
	public Authority addPermission(String permission) {
		this.permissions.add(permission);
		return this;
	}
	
}
