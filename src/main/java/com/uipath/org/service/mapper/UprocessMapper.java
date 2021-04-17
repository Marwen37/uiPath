package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.UprocessDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Uprocess} and its DTO {@link UprocessDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UprocessMapper extends EntityMapper<UprocessDTO, Uprocess> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UprocessDTO toDtoId(Uprocess uprocess);
}
