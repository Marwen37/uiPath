package com.uipath.org.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UjobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UjobDTO.class);
        UjobDTO ujobDTO1 = new UjobDTO();
        ujobDTO1.setId(1L);
        UjobDTO ujobDTO2 = new UjobDTO();
        assertThat(ujobDTO1).isNotEqualTo(ujobDTO2);
        ujobDTO2.setId(ujobDTO1.getId());
        assertThat(ujobDTO1).isEqualTo(ujobDTO2);
        ujobDTO2.setId(2L);
        assertThat(ujobDTO1).isNotEqualTo(ujobDTO2);
        ujobDTO1.setId(null);
        assertThat(ujobDTO1).isNotEqualTo(ujobDTO2);
    }
}
