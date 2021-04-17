package com.uipath.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Environment.
 */
@Entity
@Table(name = "environment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Environment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "environment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "machines", "environment", "environment", "machines" }, allowSetters = true)
    private Set<Robot> robots = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "upackages", "environments", "upackages", "environments" }, allowSetters = true)
    private Process process;

    @ManyToOne
    @JsonIgnoreProperties(value = { "upackages", "environments", "upackages", "environments" }, allowSetters = true)
    private Process process;

    @OneToMany(mappedBy = "environment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "machines", "environment", "environment", "machines" }, allowSetters = true)
    private Set<Robot> robots = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Environment id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Environment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Environment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Robot> getRobots() {
        return this.robots;
    }

    public Environment robots(Set<Robot> robots) {
        this.setRobots(robots);
        return this;
    }

    public Environment addRobot(Robot robot) {
        this.robots.add(robot);
        robot.setEnvironment(this);
        return this;
    }

    public Environment removeRobot(Robot robot) {
        this.robots.remove(robot);
        robot.setEnvironment(null);
        return this;
    }

    public void setRobots(Set<Robot> robots) {
        if (this.robots != null) {
            this.robots.forEach(i -> i.setEnvironment(null));
        }
        if (robots != null) {
            robots.forEach(i -> i.setEnvironment(this));
        }
        this.robots = robots;
    }

    public Process getProcess() {
        return this.process;
    }

    public Environment process(Process process) {
        this.setProcess(process);
        return this;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Process getProcess() {
        return this.process;
    }

    public Environment process(Process process) {
        this.setProcess(process);
        return this;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Set<Robot> getRobots() {
        return this.robots;
    }

    public Environment robots(Set<Robot> robots) {
        this.setRobots(robots);
        return this;
    }

    public Environment addRobot(Robot robot) {
        this.robots.add(robot);
        robot.setEnvironment(this);
        return this;
    }

    public Environment removeRobot(Robot robot) {
        this.robots.remove(robot);
        robot.setEnvironment(null);
        return this;
    }

    public void setRobots(Set<Robot> robots) {
        if (this.robots != null) {
            this.robots.forEach(i -> i.setEnvironment(null));
        }
        if (robots != null) {
            robots.forEach(i -> i.setEnvironment(this));
        }
        this.robots = robots;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Environment)) {
            return false;
        }
        return id != null && id.equals(((Environment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Environment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
