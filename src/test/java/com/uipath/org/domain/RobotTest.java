package com.uipath.org.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.uipath.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RobotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Robot.class);
        Robot robot1 = new Robot();
        robot1.setId(1L);
        Robot robot2 = new Robot();
        robot2.setId(robot1.getId());
        assertThat(robot1).isEqualTo(robot2);
        robot2.setId(2L);
        assertThat(robot1).isNotEqualTo(robot2);
        robot1.setId(null);
        assertThat(robot1).isNotEqualTo(robot2);
    }
}
