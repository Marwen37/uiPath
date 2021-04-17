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
 * A Uprocess.
 */
@Entity
@Table(name = "uprocess")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Uprocess implements Serializable {

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

    @OneToMany(mappedBy = "uprocess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "uprocess", "uprocess" }, allowSetters = true)
    private Set<Upackage> upackages = new HashSet<>();

    @OneToMany(mappedBy = "uprocess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "urobots", "uprocess", "uprocess", "urobots" }, allowSetters = true)
    private Set<Uenvironment> uenvironments = new HashSet<>();

    @OneToMany(mappedBy = "uprocess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "uprocess", "uprocess" }, allowSetters = true)
    private Set<Upackage> upackages = new HashSet<>();

    @OneToMany(mappedBy = "uprocess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "urobots", "uprocess", "uprocess", "urobots" }, allowSetters = true)
    private Set<Uenvironment> uenvironments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Uprocess id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Uprocess name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Uprocess description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getJobPriority() {
        return this.jobPriority;
    }

    public Uprocess jobPriority(Priority jobPriority) {
        this.jobPriority = jobPriority;
        return this;
    }

    public void setJobPriority(Priority jobPriority) {
        this.jobPriority = jobPriority;
    }

    public Set<Upackage> getUpackages() {
        return this.upackages;
    }

    public Uprocess upackages(Set<Upackage> upackages) {
        this.setUpackages(upackages);
        return this;
    }

    public Uprocess addUpackage(Upackage upackage) {
        this.upackages.add(upackage);
        upackage.setUprocess(this);
        return this;
    }

    public Uprocess removeUpackage(Upackage upackage) {
        this.upackages.remove(upackage);
        upackage.setUprocess(null);
        return this;
    }

    public void setUpackages(Set<Upackage> upackages) {
        if (this.upackages != null) {
            this.upackages.forEach(i -> i.setUprocess(null));
        }
        if (upackages != null) {
            upackages.forEach(i -> i.setUprocess(this));
        }
        this.upackages = upackages;
    }

    public Set<Uenvironment> getUenvironments() {
        return this.uenvironments;
    }

    public Uprocess uenvironments(Set<Uenvironment> uenvironments) {
        this.setUenvironments(uenvironments);
        return this;
    }

    public Uprocess addUenvironment(Uenvironment uenvironment) {
        this.uenvironments.add(uenvironment);
        uenvironment.setUprocess(this);
        return this;
    }

    public Uprocess removeUenvironment(Uenvironment uenvironment) {
        this.uenvironments.remove(uenvironment);
        uenvironment.setUprocess(null);
        return this;
    }

    public void setUenvironments(Set<Uenvironment> uenvironments) {
        if (this.uenvironments != null) {
            this.uenvironments.forEach(i -> i.setUprocess(null));
        }
        if (uenvironments != null) {
            uenvironments.forEach(i -> i.setUprocess(this));
        }
        this.uenvironments = uenvironments;
    }

    public Set<Upackage> getUpackages() {
        return this.upackages;
    }

    public Uprocess upackages(Set<Upackage> upackages) {
        this.setUpackages(upackages);
        return this;
    }

    public Uprocess addUpackage(Upackage upackage) {
        this.upackages.add(upackage);
        upackage.setUprocess(this);
        return this;
    }

    public Uprocess removeUpackage(Upackage upackage) {
        this.upackages.remove(upackage);
        upackage.setUprocess(null);
        return this;
    }

    public void setUpackages(Set<Upackage> upackages) {
        if (this.upackages != null) {
            this.upackages.forEach(i -> i.setUprocess(null));
        }
        if (upackages != null) {
            upackages.forEach(i -> i.setUprocess(this));
        }
        this.upackages = upackages;
    }

    public Set<Uenvironment> getUenvironments() {
        return this.uenvironments;
    }

    public Uprocess uenvironments(Set<Uenvironment> uenvironments) {
        this.setUenvironments(uenvironments);
        return this;
    }

    public Uprocess addUenvironment(Uenvironment uenvironment) {
        this.uenvironments.add(uenvironment);
        uenvironment.setUprocess(this);
        return this;
    }

    public Uprocess removeUenvironment(Uenvironment uenvironment) {
        this.uenvironments.remove(uenvironment);
        uenvironment.setUprocess(null);
        return this;
    }

    public void setUenvironments(Set<Uenvironment> uenvironments) {
        if (this.uenvironments != null) {
            this.uenvironments.forEach(i -> i.setUprocess(null));
        }
        if (uenvironments != null) {
            uenvironments.forEach(i -> i.setUprocess(this));
        }
        this.uenvironments = uenvironments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Uprocess)) {
            return false;
        }
        return id != null && id.equals(((Uprocess) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Uprocess{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", jobPriority='" + getJobPriority() + "'" +
            "}";
    }
}
