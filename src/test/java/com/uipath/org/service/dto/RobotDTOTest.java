package com.uipath.org.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RobotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RobotDTO.class);
        RobotDTO robotDTO1 = new RobotDTO();
        robotDTO1.setId(1L);
        RobotDTO robotDTO2 = new RobotDTO();
        assertThat(robotDTO1).isNotEqualTo(robotDTO2);
        robotDTO2.setId(robotDTO1.getId());
        assertThat(robotDTO1).isEqualTo(robotDTO2);
        robotDTO2.setId(2L);
        assertThat(robotDTO1).isNotEqualTo(robotDTO2);
        robotDTO1.setId(null);
        assertThat(robotDTO1).isNotEqualTo(robotDTO2);
    }
}
