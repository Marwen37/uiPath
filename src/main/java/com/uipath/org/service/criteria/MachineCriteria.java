package com.uipath.org.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.uipath.org.domain.Machine} entity. This class is used
 * in {@link com.uipath.org.web.rest.MachineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /machines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MachineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter licence;

    private LongFilter urobotId;

    private LongFilter urobotId;

    public MachineCriteria() {}

    public MachineCriteria(MachineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.licence = other.licence == null ? null : other.licence.copy();
        this.urobotId = other.urobotId == null ? null : other.urobotId.copy();
        this.urobotId = other.urobotId == null ? null : other.urobotId.copy();
    }

    @Override
    public MachineCriteria copy() {
        return new MachineCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getLicence() {
        return licence;
    }

    public IntegerFilter licence() {
        if (licence == null) {
            licence = new IntegerFilter();
        }
        return licence;
    }

    public void setLicence(IntegerFilter licence) {
        this.licence = licence;
    }

    public LongFilter getUrobotId() {
        return urobotId;
    }

    public LongFilter urobotId() {
        if (urobotId == null) {
            urobotId = new LongFilter();
        }
        return urobotId;
    }

    public void setUrobotId(LongFilter urobotId) {
        this.urobotId = urobotId;
    }

    public LongFilter getUrobotId() {
        return urobotId;
    }

    public LongFilter urobotId() {
        if (urobotId == null) {
            urobotId = new LongFilter();
        }
        return urobotId;
    }

    public void setUrobotId(LongFilter urobotId) {
        this.urobotId = urobotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MachineCriteria that = (MachineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(licence, that.licence) &&
            Objects.equals(urobotId, that.urobotId) &&
            Objects.equals(urobotId, that.urobotId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, licence, urobotId, urobotId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MachineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (licence != null ? "licence=" + licence + ", " : "") +
            (urobotId != null ? "urobotId=" + urobotId + ", " : "") +
            (urobotId != null ? "urobotId=" + urobotId + ", " : "") +
            "}";
    }
}
