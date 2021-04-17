package com.uipath.org.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UenvironmentMapperTest {

    private UenvironmentMapper uenvironmentMapper;

    @BeforeEach
    public void setUp() {
        uenvironmentMapper = new UenvironmentMapperImpl();
    }
}
