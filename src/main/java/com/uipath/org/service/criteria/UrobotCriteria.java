package com.uipath.org.service.criteria;

import com.uipath.org.domain.enumeration.RobotType;
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
 * Criteria class for the {@link com.uipath.org.domain.Urobot} entity. This class is used
 * in {@link com.uipath.org.web.rest.UrobotResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /urobots?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UrobotCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RobotType
     */
    public static class RobotTypeFilter extends Filter<RobotType> {

        public RobotTypeFilter() {}

        public RobotTypeFilter(RobotTypeFilter filter) {
            super(filter);
        }

        @Override
        public RobotTypeFilter copy() {
            return new RobotTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private RobotTypeFilter type;

    private StringFilter domainUsername;

    private StringFilter password;

    private LongFilter uenvironmentId;

    private LongFilter machineId;

    public UrobotCriteria() {}

    public UrobotCriteria(UrobotCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.domainUsername = other.domainUsername == null ? null : other.domainUsername.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.uenvironmentId = other.uenvironmentId == null ? null : other.uenvironmentId.copy();
        this.machineId = other.machineId == null ? null : other.machineId.copy();
    }

    @Override
    public UrobotCriteria copy() {
        return new UrobotCriteria(this);
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

    public RobotTypeFilter getType() {
        return type;
    }

    public RobotTypeFilter type() {
        if (type == null) {
            type = new RobotTypeFilter();
        }
        return type;
    }

    public void setType(RobotTypeFilter type) {
        this.type = type;
    }

    public StringFilter getDomainUsername() {
        return domainUsername;
    }

    public StringFilter domainUsername() {
        if (domainUsername == null) {
            domainUsername = new StringFilter();
        }
        return domainUsername;
    }

    public void setDomainUsername(StringFilter domainUsername) {
        this.domainUsername = domainUsername;
    }

    public StringFilter getPassword() {
        return password;
    }

    public StringFilter password() {
        if (password == null) {
            password = new StringFilter();
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public LongFilter getUenvironmentId() {
        return uenvironmentId;
    }

    public LongFilter uenvironmentId() {
        if (uenvironmentId == null) {
            uenvironmentId = new LongFilter();
        }
        return uenvironmentId;
    }

    public void setUenvironmentId(LongFilter uenvironmentId) {
        this.uenvironmentId = uenvironmentId;
    }

    public LongFilter getMachineId() {
        return machineId;
    }

    public LongFilter machineId() {
        if (machineId == null) {
            machineId = new LongFilter();
        }
        return machineId;
    }

    public void setMachineId(LongFilter machineId) {
        this.machineId = machineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UrobotCriteria that = (UrobotCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(type, that.type) &&
            Objects.equals(domainUsername, that.domainUsername) &&
            Objects.equals(password, that.password) &&
            Objects.equals(uenvironmentId, that.uenvironmentId) &&
            Objects.equals(machineId, that.machineId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, domainUsername, password, uenvironmentId, machineId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UrobotCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (domainUsername != null ? "domainUsername=" + domainUsername + ", " : "") +
            (password != null ? "password=" + password + ", " : "") +
            (uenvironmentId != null ? "uenvironmentId=" + uenvironmentId + ", " : "") +
            (machineId != null ? "machineId=" + machineId + ", " : "") +
            "}";
    }
}
