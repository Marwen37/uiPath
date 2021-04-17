package com.uipath.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uipath.org.domain.enumeration.RobotType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Robot.
 */
@Entity
@Table(name = "robot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Robot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private RobotType type;

    @Column(name = "domain_username")
    private String domainUsername;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "robot")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "robot", "robot" }, allowSetters = true)
    private Set<Machine> machines = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "robots", "process", "process", "robots" }, allowSetters = true)
    private Environment environment;

    @ManyToOne
    @JsonIgnoreProperties(value = { "robots", "process", "process", "robots" }, allowSetters = true)
    private Environment environment;

    @OneToMany(mappedBy = "robot")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "robot", "robot" }, allowSetters = true)
    private Set<Machine> machines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Robot id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Robot name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Robot description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RobotType getType() {
        return this.type;
    }

    public Robot type(RobotType type) {
        this.type = type;
        return this;
    }

    public void setType(RobotType type) {
        this.type = type;
    }

    public String getDomainUsername() {
        return this.domainUsername;
    }

    public Robot domainUsername(String domainUsername) {
        this.domainUsername = domainUsername;
        return this;
    }

    public void setDomainUsername(String domainUsername) {
        this.domainUsername = domainUsername;
    }

    public String getPassword() {
        return this.password;
    }

    public Robot password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Machine> getMachines() {
        return this.machines;
    }

    public Robot machines(Set<Machine> machines) {
        this.setMachines(machines);
        return this;
    }

    public Robot addMachine(Machine machine) {
        this.machines.add(machine);
        machine.setRobot(this);
        return this;
    }

    public Robot removeMachine(Machine machine) {
        this.machines.remove(machine);
        machine.setRobot(null);
        return this;
    }

    public void setMachines(Set<Machine> machines) {
        if (this.machines != null) {
            this.machines.forEach(i -> i.setRobot(null));
        }
        if (machines != null) {
            machines.forEach(i -> i.setRobot(this));
        }
        this.machines = machines;
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public Robot environment(Environment environment) {
        this.setEnvironment(environment);
        return this;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public Robot environment(Environment environment) {
        this.setEnvironment(environment);
        return this;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Set<Machine> getMachines() {
        return this.machines;
    }

    public Robot machines(Set<Machine> machines) {
        this.setMachines(machines);
        return this;
    }

    public Robot addMachine(Machine machine) {
        this.machines.add(machine);
        machine.setRobot(this);
        return this;
    }

    public Robot removeMachine(Machine machine) {
        this.machines.remove(machine);
        machine.setRobot(null);
        return this;
    }

    public void setMachines(Set<Machine> machines) {
        if (this.machines != null) {
            this.machines.forEach(i -> i.setRobot(null));
        }
        if (machines != null) {
            machines.forEach(i -> i.setRobot(this));
        }
        this.machines = machines;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Robot)) {
            return false;
        }
        return id != null && id.equals(((Robot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Robot{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", domainUsername='" + getDomainUsername() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
