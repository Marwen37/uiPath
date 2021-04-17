package com.uipath.org.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.uipath.org.domain.Environment} entity.
 */
public class EnvironmentDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private ProcessDTO process;

    private ProcessDTO process;

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

    public ProcessDTO getProcess() {
        return process;
    }

    public void setProcess(ProcessDTO process) {
        this.process = process;
    }

    public ProcessDTO getProcess() {
        return process;
    }

    public void setProcess(ProcessDTO process) {
        this.process = process;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnvironmentDTO)) {
            return false;
        }

        EnvironmentDTO environmentDTO = (EnvironmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, environmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnvironmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", process=" + getProcess() +
            ", process=" + getProcess() +
            "}";
    }
}
