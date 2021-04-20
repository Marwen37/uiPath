package com.uipath.org.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UenvironmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UenvironmentDTO.class);
        UenvironmentDTO uenvironmentDTO1 = new UenvironmentDTO();
        uenvironmentDTO1.setId(1L);
        UenvironmentDTO uenvironmentDTO2 = new UenvironmentDTO();
        assertThat(uenvironmentDTO1).isNotEqualTo(uenvironmentDTO2);
        uenvironmentDTO2.setId(uenvironmentDTO1.getId());
        assertThat(uenvironmentDTO1).isEqualTo(uenvironmentDTO2);
        uenvironmentDTO2.setId(2L);
        assertThat(uenvironmentDTO1).isNotEqualTo(uenvironmentDTO2);
        uenvironmentDTO1.setId(null);
        assertThat(uenvironmentDTO1).isNotEqualTo(uenvironmentDTO2);
    }
}
