package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.UenvironmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Uenvironment} and its DTO {@link UenvironmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { UprocessMapper.class })
public interface UenvironmentMapper extends EntityMapper<UenvironmentDTO, Uenvironment> {
    @Mapping(target = "uprocess", source = "uprocess", qualifiedByName = "id")
    UenvironmentDTO toDto(Uenvironment s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UenvironmentDTO toDtoId(Uenvironment uenvironment);
}
