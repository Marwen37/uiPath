package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.ProcessDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Process} and its DTO {@link ProcessDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProcessMapper extends EntityMapper<ProcessDTO, Process> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProcessDTO toDtoId(Process process);
}
