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
 * Criteria class for the {@link com.uipath.org.domain.Uprocess} entity. This class is used
 * in {@link com.uipath.org.web.rest.UprocessResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /uprocesses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UprocessCriteria implements Serializable, Criteria {

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

    private LongFilter uenvironmentId;

    public UprocessCriteria() {}

    public UprocessCriteria(UprocessCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.jobPriority = other.jobPriority == null ? null : other.jobPriority.copy();
        this.upackageId = other.upackageId == null ? null : other.upackageId.copy();
        this.uenvironmentId = other.uenvironmentId == null ? null : other.uenvironmentId.copy();
    }

    @Override
    public UprocessCriteria copy() {
        return new UprocessCriteria(this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UprocessCriteria that = (UprocessCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(jobPriority, that.jobPriority) &&
            Objects.equals(upackageId, that.upackageId) &&
            Objects.equals(uenvironmentId, that.uenvironmentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, jobPriority, upackageId, uenvironmentId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UprocessCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (jobPriority != null ? "jobPriority=" + jobPriority + ", " : "") +
            (upackageId != null ? "upackageId=" + upackageId + ", " : "") +
            (uenvironmentId != null ? "uenvironmentId=" + uenvironmentId + ", " : "") +
            "}";
    }
}
