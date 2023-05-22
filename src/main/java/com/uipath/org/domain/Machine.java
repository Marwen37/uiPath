package com.uipath.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Machine.
 */
@Entity
@Table(name = "machine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Machine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;
// Add commentaire pour pull from main
    @Column(name = "description")
    private String description;

    @Column(name = "licence")
    private Integer licence;

    @ManyToOne
    @JsonIgnoreProperties(value = { "uenvironment", "machines" }, allowSetters = true)
    private Urobot urobot;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Commentaire pour push sur le serveur 
    public Machine id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Machine name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    // 2em jour : ajouter 2em commentaire
    public String getDescription() {
        return this.description;
    }

    public Machine description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLicence() {
        return this.licence;
    }

    public Machine licence(Integer licence) {
        this.licence = licence;
        return this;
    }

    public void setLicence(Integer licence) {
        this.licence = licence;
    }

    public Urobot getUrobot() {
        return this.urobot;
    }

    public Machine urobot(Urobot urobot) {
        this.setUrobot(urobot);
        return this;
    }

    public void setUrobot(Urobot urobot) {
        this.urobot = urobot;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Machine)) {
            return false;
        }
        return id != null && id.equals(((Machine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Machine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", licence=" + getLicence() +
            "}";
    }
}
