package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.JobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Job} and its DTO {@link JobDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobMapper extends EntityMapper<JobDTO, Job> {}
