package com.uipath.org.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RobotMapperTest {

    private RobotMapper robotMapper;

    @BeforeEach
    public void setUp() {
        robotMapper = new RobotMapperImpl();
    }
}
