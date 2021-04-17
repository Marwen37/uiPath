package com.uipath.org.service.criteria;

import com.uipath.org.domain.enumeration.Priority;
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
 * Criteria class for the {@link com.uipath.org.domain.Process} entity. This class is used
 * in {@link com.uipath.org.web.rest.ProcessResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /processes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProcessCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Priority
     */
    public static class PriorityFilter extends Filter<Priority> {

        public PriorityFilter() {}

        public PriorityFilter(PriorityFilter filter) {
            super(filter);
        }

        @Override
        public PriorityFilter copy() {
            return new PriorityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private PriorityFilter jobPriority;

    private LongFilter upackageId;

    private LongFilter environmentId;

    private LongFilter upackageId;

    private LongFilter environmentId;

    public ProcessCriteria() {}

    public ProcessCriteria(ProcessCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.jobPriority = other.jobPriority == null ? null : other.jobPriority.copy();
        this.upackageId = other.upackageId == null ? null : other.upackageId.copy();
        this.environmentId = other.environmentId == null ? null : other.environmentId.copy();
        this.upackageId = other.upackageId == null ? null : other.upackageId.copy();
        this.environmentId = other.environmentId == null ? null : other.environmentId.copy();
    }

    @Override
    public ProcessCriteria copy() {
        return new ProcessCriteria(this);
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

    public PriorityFilter getJobPriority() {
        return jobPriority;
    }

    public PriorityFilter jobPriority() {
        if (jobPriority == null) {
            jobPriority = new PriorityFilter();
        }
        return jobPriority;
    }

    public void setJobPriority(PriorityFilter jobPriority) {
        this.jobPriority = jobPriority;
    }

    public LongFilter getUpackageId() {
        return upackageId;
    }

    public LongFilter upackageId() {
        if (upackageId == null) {
            upackageId = new LongFilter();
        }
        return upackageId;
    }

    public void setUpackageId(LongFilter upackageId) {
        this.upackageId = upackageId;
    }

    public LongFilter getEnvironmentId() {
        return environmentId;
    }

    public LongFilter environmentId() {
        if (environmentId == null) {
            environmentId = new LongFilter();
        }
        return environmentId;
    }

    public void setEnvironmentId(LongFilter environmentId) {
        this.environmentId = environmentId;
    }

    public LongFilter getUpackageId() {
        return upackageId;
    }

    public LongFilter upackageId() {
        if (upackageId == null) {
            upackageId = new LongFilter();
        }
        return upackageId;
    }

    public void setUpackageId(LongFilter upackageId) {
        this.upackageId = upackageId;
    }

    public LongFilter getEnvironmentId() {
        return environmentId;
    }

    public LongFilter environmentId() {
        if (environmentId == null) {
            environmentId = new LongFilter();
        }
        return environmentId;
    }

    public void setEnvironmentId(LongFilter environmentId) {
        this.environmentId = environmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProcessCriteria that = (ProcessCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(jobPriority, that.jobPriority) &&
            Objects.equals(upackageId, that.upackageId) &&
            Objects.equals(environmentId, that.environmentId) &&
            Objects.equals(upackageId, that.upackageId) &&
            Objects.equals(environmentId, that.environmentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, jobPriority, upackageId, environmentId, upackageId, environmentId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (jobPriority != null ? "jobPriority=" + jobPriority + ", " : "") +
            (upackageId != null ? "upackageId=" + upackageId + ", " : "") +
            (environmentId != null ? "environmentId=" + environmentId + ", " : "") +
            (upackageId != null ? "upackageId=" + upackageId + ", " : "") +
            (environmentId != null ? "environmentId=" + environmentId + ", " : "") +
            "}";
    }
}
