package com.uipath.org.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UprocessDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UprocessDTO.class);
        UprocessDTO uprocessDTO1 = new UprocessDTO();
        uprocessDTO1.setId(1L);
        UprocessDTO uprocessDTO2 = new UprocessDTO();
        assertThat(uprocessDTO1).isNotEqualTo(uprocessDTO2);
        uprocessDTO2.setId(uprocessDTO1.getId());
        assertThat(uprocessDTO1).isEqualTo(uprocessDTO2);
        uprocessDTO2.setId(2L);
        assertThat(uprocessDTO1).isNotEqualTo(uprocessDTO2);
        uprocessDTO1.setId(null);
        assertThat(uprocessDTO1).isNotEqualTo(uprocessDTO2);
    }
}
