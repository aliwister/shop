package com.badals.shop.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.shop.web.rest.TestUtil;

public class CarrierTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Carrier.class);
        Carrier carrier1 = new Carrier();
        carrier1.setId(1L);
        Carrier carrier2 = new Carrier();
        carrier2.setId(carrier1.getId());
        assertThat(carrier1).isEqualTo(carrier2);
        carrier2.setId(2L);
        assertThat(carrier1).isNotEqualTo(carrier2);
        carrier1.setId(null);
        assertThat(carrier1).isNotEqualTo(carrier2);
    }
}
