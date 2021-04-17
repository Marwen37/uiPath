package com.uipath.org.service.dto;

import com.uipath.org.domain.enumeration.Priority;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.uipath.org.domain.Process} entity.
 */
public class ProcessDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Priority jobPriority;

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

    public Priority getJobPriority() {
        return jobPriority;
    }

    public void setJobPriority(Priority jobPriority) {
        this.jobPriority = jobPriority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessDTO)) {
            return false;
        }

        ProcessDTO processDTO = (ProcessDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, processDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", jobPriority='" + getJobPriority() + "'" +
            "}";
    }
}
