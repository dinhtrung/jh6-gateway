package com.ft.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Entry.
 */
@Document(indexName = "post")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field(type = FieldType.Keyword)
    private String name;
    
    private Integer state;

    @Field(type = FieldType.Keyword)
    private String type;
    
    @Field(type = FieldType.Object)
    private Map<String, Object> fields = new HashMap<String, Object>();
    
    @Field(type = FieldType.Object)
    private Map<String, Object> meta = new HashMap<String, Object>();
    
    @Field(type = FieldType.Keyword)
    private Set<String> tags = new HashSet<>();
    
    @Field(type = FieldType.Object)
    private Category category;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Post name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getState() {
        return state;
    }

    public Post state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public Post type(String contentType) {
        this.type = contentType;
        return this;
    }

    public void setType(String contentType) {
        this.type = contentType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        return id != null && id.equals(((Post) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Entry{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", state='" + getState() + "'" +
            ", meta='" + getMeta() + "'" +
            "}";
    }
    

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}
    
    public Post fields(Map<String, Object> fields) {
		this.fields = fields;
		return this;
	}
    
	public Map<String, Object> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}
    
    public Post meta(Map<String, Object> meta) {
		this.meta = meta;
		return this;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
    
    
}
