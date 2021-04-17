package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.MachineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Machine} and its DTO {@link MachineDTO}.
 */
@Mapper(componentModel = "spring", uses = { RobotMapper.class })
public interface MachineMapper extends EntityMapper<MachineDTO, Machine> {
    @Mapping(target = "robot", source = "robot", qualifiedByName = "id")
    @Mapping(target = "robot", source = "robot", qualifiedByName = "id")
    MachineDTO toDto(Machine s);
}
