package com.uipath.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Uenvironment.
 */
@Entity
@Table(name = "uenvironment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Uenvironment implements Serializable {

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
    @JsonIgnoreProperties(value = { "upackages", "uenvironments" }, allowSetters = true)
    private Uprocess uprocess;

    @OneToMany(mappedBy = "uenvironment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "uenvironment", "machines" }, allowSetters = true)
    private Set<Urobot> urobots = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Uenvironment id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Uenvironment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Uenvironment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uprocess getUprocess() {
        return this.uprocess;
    }

    public Uenvironment uprocess(Uprocess uprocess) {
        this.setUprocess(uprocess);
        return this;
    }

    public void setUprocess(Uprocess uprocess) {
        this.uprocess = uprocess;
    }

    public Set<Urobot> getUrobots() {
        return this.urobots;
    }

    public Uenvironment urobots(Set<Urobot> urobots) {
        this.setUrobots(urobots);
        return this;
    }

    public Uenvironment addUrobot(Urobot urobot) {
        this.urobots.add(urobot);
        urobot.setUenvironment(this);
        return this;
    }

    public Uenvironment removeUrobot(Urobot urobot) {
        this.urobots.remove(urobot);
        urobot.setUenvironment(null);
        return this;
    }

    public void setUrobots(Set<Urobot> urobots) {
        if (this.urobots != null) {
            this.urobots.forEach(i -> i.setUenvironment(null));
        }
        if (urobots != null) {
            urobots.forEach(i -> i.setUenvironment(this));
        }
        this.urobots = urobots;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Uenvironment)) {
            return false;
        }
        return id != null && id.equals(((Uenvironment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Uenvironment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
