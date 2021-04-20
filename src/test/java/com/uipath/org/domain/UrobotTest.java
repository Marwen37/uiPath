package com.uipath.org.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UrobotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Urobot.class);
        Urobot urobot1 = new Urobot();
        urobot1.setId(1L);
        Urobot urobot2 = new Urobot();
        urobot2.setId(urobot1.getId());
        assertThat(urobot1).isEqualTo(urobot2);
        urobot2.setId(2L);
        assertThat(urobot1).isNotEqualTo(urobot2);
        urobot1.setId(null);
        assertThat(urobot1).isNotEqualTo(urobot2);
    }
}
