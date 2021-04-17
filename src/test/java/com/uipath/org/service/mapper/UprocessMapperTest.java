package com.uipath.org.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UprocessMapperTest {

    private UprocessMapper uprocessMapper;

    @BeforeEach
    public void setUp() {
        uprocessMapper = new UprocessMapperImpl();
    }
}
