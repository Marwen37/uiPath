package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.MachineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Machine} and its DTO {@link MachineDTO}.
 */
@Mapper(componentModel = "spring", uses = { UrobotMapper.class })
public interface MachineMapper extends EntityMapper<MachineDTO, Machine> {
    @Mapping(target = "urobot", source = "urobot", qualifiedByName = "id")
    MachineDTO toDto(Machine s);
}
