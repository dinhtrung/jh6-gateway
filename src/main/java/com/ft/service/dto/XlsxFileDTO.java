package com.ft.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO for the GmpXlsFile entity.
 */
public class XlsxFileDTO implements Serializable {

    private String id;

    private ZonedDateTime createdAt;

    private byte[] file;

    private String fileContentType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        XlsxFileDTO gmpXlsFileDTO = (XlsxFileDTO) o;
        if (gmpXlsFileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gmpXlsFileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GmpXlsFileDTO{" +
            "meta=" + getMeta() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", file='" + getFile() + "'" +
            "}";
    }
    
    private Map<String, Object> meta = new HashMap<String, Object>();

	public Map<String, Object> getMeta() {
		return meta;
	}
	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}
}
