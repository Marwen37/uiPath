package com.uipath.org.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UprocessTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Uprocess.class);
        Uprocess uprocess1 = new Uprocess();
        uprocess1.setId(1L);
        Uprocess uprocess2 = new Uprocess();
        uprocess2.setId(uprocess1.getId());
        assertThat(uprocess1).isEqualTo(uprocess2);
        uprocess2.setId(2L);
        assertThat(uprocess1).isNotEqualTo(uprocess2);
        uprocess1.setId(null);
        assertThat(uprocess1).isNotEqualTo(uprocess2);
    }
}
