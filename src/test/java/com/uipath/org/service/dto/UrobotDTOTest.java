package com.uipath.org.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UrobotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UrobotDTO.class);
        UrobotDTO urobotDTO1 = new UrobotDTO();
        urobotDTO1.setId(1L);
        UrobotDTO urobotDTO2 = new UrobotDTO();
        assertThat(urobotDTO1).isNotEqualTo(urobotDTO2);
        urobotDTO2.setId(urobotDTO1.getId());
        assertThat(urobotDTO1).isEqualTo(urobotDTO2);
        urobotDTO2.setId(2L);
        assertThat(urobotDTO1).isNotEqualTo(urobotDTO2);
        urobotDTO1.setId(null);
        assertThat(urobotDTO1).isNotEqualTo(urobotDTO2);
    }
}
