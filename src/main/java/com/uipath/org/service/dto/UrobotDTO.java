package com.uipath.org.service.dto;

import com.uipath.org.domain.enumeration.RobotType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.uipath.org.domain.Urobot} entity.
 */
public class UrobotDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private RobotType type;

    private String domainUsername;

    private String password;

    private UenvironmentDTO uenvironment;

    private UenvironmentDTO uenvironment;

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

    public RobotType getType() {
        return type;
    }

    public void setType(RobotType type) {
        this.type = type;
    }

    public String getDomainUsername() {
        return domainUsername;
    }

    public void setDomainUsername(String domainUsername) {
        this.domainUsername = domainUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UenvironmentDTO getUenvironment() {
        return uenvironment;
    }

    public void setUenvironment(UenvironmentDTO uenvironment) {
        this.uenvironment = uenvironment;
    }

    public UenvironmentDTO getUenvironment() {
        return uenvironment;
    }

    public void setUenvironment(UenvironmentDTO uenvironment) {
        this.uenvironment = uenvironment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UrobotDTO)) {
            return false;
        }

        UrobotDTO urobotDTO = (UrobotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, urobotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UrobotDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", domainUsername='" + getDomainUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", uenvironment=" + getUenvironment() +
            ", uenvironment=" + getUenvironment() +
            "}";
    }
}
