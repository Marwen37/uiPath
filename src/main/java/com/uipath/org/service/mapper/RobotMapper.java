package com.uipath.org.service.mapper;

import com.uipath.org.domain.*;
import com.uipath.org.service.dto.RobotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Robot} and its DTO {@link RobotDTO}.
 */
@Mapper(componentModel = "spring", uses = { EnvironmentMapper.class })
public interface RobotMapper extends EntityMapper<RobotDTO, Robot> {
    @Mapping(target = "environment", source = "environment", qualifiedByName = "id")
    @Mapping(target = "environment", source = "environment", qualifiedByName = "id")
    RobotDTO toDto(Robot s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RobotDTO toDtoId(Robot robot);
}
