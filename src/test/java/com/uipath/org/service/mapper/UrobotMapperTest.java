package com.uipath.org.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrobotMapperTest {

    private UrobotMapper urobotMapper;

    @BeforeEach
    public void setUp() {
        urobotMapper = new UrobotMapperImpl();
    }
}
