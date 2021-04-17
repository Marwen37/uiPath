package com.uipath.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uipath.org.domain.enumeration.Priority;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Process.
 */
@Entity
@Table(name = "process")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Process implements Serializable {

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
    @Column(name = "job_priority")
    private Priority jobPriority;

    @OneToMany(mappedBy = "process")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "process", "process" }, allowSetters = true)
    private Set<Upackage> upackages = new HashSet<>();

    @OneToMany(mappedBy = "process")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "robots", "process", "process", "robots" }, allowSetters = true)
    private Set<Environment> environments = new HashSet<>();

    @OneToMany(mappedBy = "process")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "process", "process" }, allowSetters = true)
    private Set<Upackage> upackages = new HashSet<>();

    @OneToMany(mappedBy = "process")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "robots", "process", "process", "robots" }, allowSetters = true)
    private Set<Environment> environments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Process id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Process name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Process description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getJobPriority() {
        return this.jobPriority;
    }

    public Process jobPriority(Priority jobPriority) {
        this.jobPriority = jobPriority;
        return this;
    }

    public void setJobPriority(Priority jobPriority) {
        this.jobPriority = jobPriority;
    }

    public Set<Upackage> getUpackages() {
        return this.upackages;
    }

    public Process upackages(Set<Upackage> upackages) {
        this.setUpackages(upackages);
        return this;
    }

    public Process addUpackage(Upackage upackage) {
        this.upackages.add(upackage);
        upackage.setProcess(this);
        return this;
    }

    public Process removeUpackage(Upackage upackage) {
        this.upackages.remove(upackage);
        upackage.setProcess(null);
        return this;
    }

    public void setUpackages(Set<Upackage> upackages) {
        if (this.upackages != null) {
            this.upackages.forEach(i -> i.setProcess(null));
        }
        if (upackages != null) {
            upackages.forEach(i -> i.setProcess(this));
        }
        this.upackages = upackages;
    }

    public Set<Environment> getEnvironments() {
        return this.environments;
    }

    public Process environments(Set<Environment> environments) {
        this.setEnvironments(environments);
        return this;
    }

    public Process addEnvironment(Environment environment) {
        this.environments.add(environment);
        environment.setProcess(this);
        return this;
    }

    public Process removeEnvironment(Environment environment) {
        this.environments.remove(environment);
        environment.setProcess(null);
        return this;
    }

    public void setEnvironments(Set<Environment> environments) {
        if (this.environments != null) {
            this.environments.forEach(i -> i.setProcess(null));
        }
        if (environments != null) {
            environments.forEach(i -> i.setProcess(this));
        }
        this.environments = environments;
    }

    public Set<Upackage> getUpackages() {
        return this.upackages;
    }

    public Process upackages(Set<Upackage> upackages) {
        this.setUpackages(upackages);
        return this;
    }

    public Process addUpackage(Upackage upackage) {
        this.upackages.add(upackage);
        upackage.setProcess(this);
        return this;
    }

    public Process removeUpackage(Upackage upackage) {
        this.upackages.remove(upackage);
        upackage.setProcess(null);
        return this;
    }

    public void setUpackages(Set<Upackage> upackages) {
        if (this.upackages != null) {
            this.upackages.forEach(i -> i.setProcess(null));
        }
        if (upackages != null) {
            upackages.forEach(i -> i.setProcess(this));
        }
        this.upackages = upackages;
    }

    public Set<Environment> getEnvironments() {
        return this.environments;
    }

    public Process environments(Set<Environment> environments) {
        this.setEnvironments(environments);
        return this;
    }

    public Process addEnvironment(Environment environment) {
        this.environments.add(environment);
        environment.setProcess(this);
        return this;
    }

    public Process removeEnvironment(Environment environment) {
        this.environments.remove(environment);
        environment.setProcess(null);
        return this;
    }

    public void setEnvironments(Set<Environment> environments) {
        if (this.environments != null) {
            this.environments.forEach(i -> i.setProcess(null));
        }
        if (environments != null) {
            environments.forEach(i -> i.setProcess(this));
        }
        this.environments = environments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Process)) {
            return false;
        }
        return id != null && id.equals(((Process) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Process{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", jobPriority='" + getJobPriority() + "'" +
            "}";
    }
}
