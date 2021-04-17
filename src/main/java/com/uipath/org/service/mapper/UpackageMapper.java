package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.UpackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Upackage} and its DTO {@link UpackageDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProcessMapper.class })
public interface UpackageMapper extends EntityMapper<UpackageDTO, Upackage> {
    @Mapping(target = "process", source = "process", qualifiedByName = "id")
    @Mapping(target = "process", source = "process", qualifiedByName = "id")
    UpackageDTO toDto(Upackage s);
}
