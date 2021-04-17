package com.uipath.org.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.uipath.org.domain.Machine} entity.
 */
public class MachineDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Integer licence;

    private RobotDTO robot;

    private RobotDTO robot;

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

    public Integer getLicence() {
        return licence;
    }

    public void setLicence(Integer licence) {
        this.licence = licence;
    }

    public RobotDTO getRobot() {
        return robot;
    }

    public void setRobot(RobotDTO robot) {
        this.robot = robot;
    }

    public RobotDTO getRobot() {
        return robot;
    }

    public void setRobot(RobotDTO robot) {
        this.robot = robot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MachineDTO)) {
            return false;
        }

        MachineDTO machineDTO = (MachineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, machineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MachineDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", licence=" + getLicence() +
            ", robot=" + getRobot() +
            ", robot=" + getRobot() +
            "}";
    }
}
