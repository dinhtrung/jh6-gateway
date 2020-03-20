package com.ft.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Entry.
 */
@Document(collection = "node")
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;
    
    @Indexed(unique = true)
    @Field("slug")
    private String slug;
    
    @Field("state")
    private Integer state;

    @Field("type")
    @Indexed
    private String type;
    
    @Field("fields")
    @Indexed
    private Map<String, Object> fields = new HashMap<String, Object>();
    
    @Field("meta")
    private Map<String, Object> meta = new HashMap<String, Object>();
    
    @Indexed
    private Set<String> tags = new HashSet<>();
    
    @Field("created_at")
    @CreatedDate
    private Instant createdAt = Instant.now();

    @Field("created_by")
    @CreatedBy
    private String createdBy;

    @Field("updated_at")
    @LastModifiedDate
    private Instant updatedAt = Instant.now();

    @Field("updated_by")
    @LastModifiedBy
    private String updatedBy;
    
    @Field("touched_by")
    private Set<String> touchedBy = new HashSet<>(); 
    
    
    
    @Override
    public String toString() {
        return "Entry{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", state='" + getState() + "'" +
            ", meta='" + getMeta() + "'" +
            "}";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Node name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getState() {
        return state;
    }

    public Node state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public Node type(String contentType) {
        this.type = contentType;
        return this;
    }

    public void setType(String contentType) {
        this.type = contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        return id != null && id.equals(((Node) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}
    
    public Node fields(Map<String, Object> fields) {
		this.fields = fields;
		return this;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}
    
    public Node meta(Map<String, Object> meta) {
		this.meta = meta;
		return this;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
	public Node createdAt(Instant createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public Node updatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
		return this;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public Node updatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
		return this;
	}

	public Set<String> getTouchedBy() {
		return touchedBy;
	}

	public void setTouchedBy(Set<String> touchedBy) {
		this.touchedBy = touchedBy;
	}
	
	public Node touchedBy(String touchedBy) {
		this.touchedBy.add(touchedBy);
		return this;
	}

}
