package com.uipath.org.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UjobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ujob.class);
        Ujob ujob1 = new Ujob();
        ujob1.setId(1L);
        Ujob ujob2 = new Ujob();
        ujob2.setId(ujob1.getId());
        assertThat(ujob1).isEqualTo(ujob2);
        ujob2.setId(2L);
        assertThat(ujob1).isNotEqualTo(ujob2);
        ujob1.setId(null);
        assertThat(ujob1).isNotEqualTo(ujob2);
    }
}
