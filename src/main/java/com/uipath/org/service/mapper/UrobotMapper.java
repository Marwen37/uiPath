package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.UrobotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Urobot} and its DTO {@link UrobotDTO}.
 */
@Mapper(componentModel = "spring", uses = { UenvironmentMapper.class })
public interface UrobotMapper extends EntityMapper<UrobotDTO, Urobot> {
    @Mapping(target = "uenvironment", source = "uenvironment", qualifiedByName = "id")
    @Mapping(target = "uenvironment", source = "uenvironment", qualifiedByName = "id")
    UrobotDTO toDto(Urobot s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UrobotDTO toDtoId(Urobot urobot);
}
