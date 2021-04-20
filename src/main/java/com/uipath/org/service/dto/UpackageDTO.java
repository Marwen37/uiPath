package com.uipath.org.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.uipath.org.domain.Upackage} entity.
 */
public class UpackageDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private UprocessDTO uprocess;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UprocessDTO getUprocess() {
        return uprocess;
    }

    public void setUprocess(UprocessDTO uprocess) {
        this.uprocess = uprocess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UpackageDTO)) {
            return false;
        }

        UpackageDTO upackageDTO = (UpackageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, upackageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UpackageDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", uprocess=" + getUprocess() +
            "}";
    }
}
