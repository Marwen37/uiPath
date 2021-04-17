package com.uipath.org.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpackageMapperTest {

    private UpackageMapper upackageMapper;

    @BeforeEach
    public void setUp() {
        upackageMapper = new UpackageMapperImpl();
    }
}
