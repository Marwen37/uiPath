package com.uipath.org.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UpackageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UpackageDTO.class);
        UpackageDTO upackageDTO1 = new UpackageDTO();
        upackageDTO1.setId(1L);
        UpackageDTO upackageDTO2 = new UpackageDTO();
        assertThat(upackageDTO1).isNotEqualTo(upackageDTO2);
        upackageDTO2.setId(upackageDTO1.getId());
        assertThat(upackageDTO1).isEqualTo(upackageDTO2);
        upackageDTO2.setId(2L);
        assertThat(upackageDTO1).isNotEqualTo(upackageDTO2);
        upackageDTO1.setId(null);
        assertThat(upackageDTO1).isNotEqualTo(upackageDTO2);
    }
}
