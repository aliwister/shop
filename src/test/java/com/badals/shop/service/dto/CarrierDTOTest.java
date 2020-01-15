package com.badals.shop.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.shop.web.rest.TestUtil;

public class CarrierDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarrierDTO.class);
        CarrierDTO carrierDTO1 = new CarrierDTO();
        carrierDTO1.setId(1L);
        CarrierDTO carrierDTO2 = new CarrierDTO();
        assertThat(carrierDTO1).isNotEqualTo(carrierDTO2);
        carrierDTO2.setId(carrierDTO1.getId());
        assertThat(carrierDTO1).isEqualTo(carrierDTO2);
        carrierDTO2.setId(2L);
        assertThat(carrierDTO1).isNotEqualTo(carrierDTO2);
        carrierDTO1.setId(null);
        assertThat(carrierDTO1).isNotEqualTo(carrierDTO2);
    }
}
