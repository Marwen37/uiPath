package com.uipath.org.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UpackageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Upackage.class);
        Upackage upackage1 = new Upackage();
        upackage1.setId(1L);
        Upackage upackage2 = new Upackage();
        upackage2.setId(upackage1.getId());
        assertThat(upackage1).isEqualTo(upackage2);
        upackage2.setId(2L);
        assertThat(upackage1).isNotEqualTo(upackage2);
        upackage1.setId(null);
        assertThat(upackage1).isNotEqualTo(upackage2);
    }
}
