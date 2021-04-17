package com.uipath.org.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UenvironmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Uenvironment.class);
        Uenvironment uenvironment1 = new Uenvironment();
        uenvironment1.setId(1L);
        Uenvironment uenvironment2 = new Uenvironment();
        uenvironment2.setId(uenvironment1.getId());
        assertThat(uenvironment1).isEqualTo(uenvironment2);
        uenvironment2.setId(2L);
        assertThat(uenvironment1).isNotEqualTo(uenvironment2);
        uenvironment1.setId(null);
        assertThat(uenvironment1).isNotEqualTo(uenvironment2);
    }
}
