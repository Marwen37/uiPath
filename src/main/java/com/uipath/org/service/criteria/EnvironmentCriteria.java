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
 * Criteria class for the {@link com.uipath.org.domain.Environment} entity. This class is used
 * in {@link com.uipath.org.web.rest.EnvironmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /environments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EnvironmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private LongFilter robotId;

    private LongFilter processId;

    private LongFilter processId;

    private LongFilter robotId;

    public EnvironmentCriteria() {}

    public EnvironmentCriteria(EnvironmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.robotId = other.robotId == null ? null : other.robotId.copy();
        this.processId = other.processId == null ? null : other.processId.copy();
        this.processId = other.processId == null ? null : other.processId.copy();
        this.robotId = other.robotId == null ? null : other.robotId.copy();
    }

    @Override
    public EnvironmentCriteria copy() {
        return new EnvironmentCriteria(this);
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

    public LongFilter getRobotId() {
        return robotId;
    }

    public LongFilter robotId() {
        if (robotId == null) {
            robotId = new LongFilter();
        }
        return robotId;
    }

    public void setRobotId(LongFilter robotId) {
        this.robotId = robotId;
    }

    public LongFilter getProcessId() {
        return processId;
    }

    public LongFilter processId() {
        if (processId == null) {
            processId = new LongFilter();
        }
        return processId;
    }

    public void setProcessId(LongFilter processId) {
        this.processId = processId;
    }

    public LongFilter getProcessId() {
        return processId;
    }

    public LongFilter processId() {
        if (processId == null) {
            processId = new LongFilter();
        }
        return processId;
    }

    public void setProcessId(LongFilter processId) {
        this.processId = processId;
    }

    public LongFilter getRobotId() {
        return robotId;
    }

    public LongFilter robotId() {
        if (robotId == null) {
            robotId = new LongFilter();
        }
        return robotId;
    }

    public void setRobotId(LongFilter robotId) {
        this.robotId = robotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EnvironmentCriteria that = (EnvironmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(robotId, that.robotId) &&
            Objects.equals(processId, that.processId) &&
            Objects.equals(processId, that.processId) &&
            Objects.equals(robotId, that.robotId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, robotId, processId, processId, robotId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnvironmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (robotId != null ? "robotId=" + robotId + ", " : "") +
            (processId != null ? "processId=" + processId + ", " : "") +
            (processId != null ? "processId=" + processId + ", " : "") +
            (robotId != null ? "robotId=" + robotId + ", " : "") +
            "}";
    }
}
