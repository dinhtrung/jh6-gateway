package com.ft.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An authority (a security role) used by Spring Security.
 */
@Document(collection = "jhi_authority")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    private String name;
    
    /** Extra fields **/
    @Field("meta")
	private Map<String, Object> meta = new HashMap<>();
    
    @Field("sites")
	private Set<String> sites = new HashSet<>();
    
    @Field("permissions")
	private Set<String> permissions = new HashSet<>();

    @Override
    public String toString() {
        return "Authority{" +
            "name='" + name + '\'' +
            "}";
    }
    
    public String getId() {
		return name;
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
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        return Objects.equals(name, ((Authority) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
