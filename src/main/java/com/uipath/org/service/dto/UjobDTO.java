package com.uipath.org.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.uipath.org.domain.Ujob} entity.
 */
public class UjobDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UjobDTO)) {
            return false;
        }

        UjobDTO ujobDTO = (UjobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ujobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UjobDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
