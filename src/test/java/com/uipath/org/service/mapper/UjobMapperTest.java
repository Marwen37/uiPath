package com.uipath.org.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UjobMapperTest {

    private UjobMapper ujobMapper;

    @BeforeEach
    public void setUp() {
        ujobMapper = new UjobMapperImpl();
    }
}
