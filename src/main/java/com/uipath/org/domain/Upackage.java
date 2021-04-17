package com.uipath.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Upackage.
 */
@Entity
@Table(name = "upackage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Upackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "upackages", "environments", "upackages", "environments" }, allowSetters = true)
    private Process process;

    @ManyToOne
    @JsonIgnoreProperties(value = { "upackages", "environments", "upackages", "environments" }, allowSetters = true)
    private Process process;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Upackage id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Upackage name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Upackage description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Process getProcess() {
        return this.process;
    }

    public Upackage process(Process process) {
        this.setProcess(process);
        return this;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Process getProcess() {
        return this.process;
    }

    public Upackage process(Process process) {
        this.setProcess(process);
        return this;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Upackage)) {
            return false;
        }
        return id != null && id.equals(((Upackage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Upackage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
