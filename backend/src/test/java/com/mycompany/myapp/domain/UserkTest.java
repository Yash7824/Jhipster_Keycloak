package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Userk.class);
        Userk userk1 = new Userk();
        userk1.setId(1L);
        Userk userk2 = new Userk();
        userk2.setId(userk1.getId());
        assertThat(userk1).isEqualTo(userk2);
        userk2.setId(2L);
        assertThat(userk1).isNotEqualTo(userk2);
        userk1.setId(null);
        assertThat(userk1).isNotEqualTo(userk2);
    }
}
