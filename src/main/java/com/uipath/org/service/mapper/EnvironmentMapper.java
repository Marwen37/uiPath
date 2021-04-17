package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.EnvironmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Environment} and its DTO {@link EnvironmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProcessMapper.class })
public interface EnvironmentMapper extends EntityMapper<EnvironmentDTO, Environment> {
    @Mapping(target = "process", source = "process", qualifiedByName = "id")
    @Mapping(target = "process", source = "process", qualifiedByName = "id")
    EnvironmentDTO toDto(Environment s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnvironmentDTO toDtoId(Environment environment);
}
