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
 * Criteria class for the {@link com.uipath.org.domain.Uenvironment} entity. This class is used
 * in {@link com.uipath.org.web.rest.UenvironmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /uenvironments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UenvironmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private LongFilter urobotId;

    private LongFilter uprocessId;

    private LongFilter uprocessId;

    private LongFilter urobotId;

    public UenvironmentCriteria() {}

    public UenvironmentCriteria(UenvironmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.urobotId = other.urobotId == null ? null : other.urobotId.copy();
        this.uprocessId = other.uprocessId == null ? null : other.uprocessId.copy();
        this.uprocessId = other.uprocessId == null ? null : other.uprocessId.copy();
        this.urobotId = other.urobotId == null ? null : other.urobotId.copy();
    }

    @Override
    public UenvironmentCriteria copy() {
        return new UenvironmentCriteria(this);
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

    public LongFilter getUprocessId() {
        return uprocessId;
    }

    public LongFilter uprocessId() {
        if (uprocessId == null) {
            uprocessId = new LongFilter();
        }
        return uprocessId;
    }

    public void setUprocessId(LongFilter uprocessId) {
        this.uprocessId = uprocessId;
    }

    public LongFilter getUprocessId() {
        return uprocessId;
    }

    public LongFilter uprocessId() {
        if (uprocessId == null) {
            uprocessId = new LongFilter();
        }
        return uprocessId;
    }

    public void setUprocessId(LongFilter uprocessId) {
        this.uprocessId = uprocessId;
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
        final UenvironmentCriteria that = (UenvironmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(urobotId, that.urobotId) &&
            Objects.equals(uprocessId, that.uprocessId) &&
            Objects.equals(uprocessId, that.uprocessId) &&
            Objects.equals(urobotId, that.urobotId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, urobotId, uprocessId, uprocessId, urobotId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UenvironmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (urobotId != null ? "urobotId=" + urobotId + ", " : "") +
            (uprocessId != null ? "uprocessId=" + uprocessId + ", " : "") +
            (uprocessId != null ? "uprocessId=" + uprocessId + ", " : "") +
            (urobotId != null ? "urobotId=" + urobotId + ", " : "") +
            "}";
    }
}
