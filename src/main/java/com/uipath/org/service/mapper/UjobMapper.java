package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.UjobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ujob} and its DTO {@link UjobDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UjobMapper extends EntityMapper<UjobDTO, Ujob> {}
