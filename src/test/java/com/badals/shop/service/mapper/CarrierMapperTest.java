package com.badals.shop.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CarrierMapperTest {

    private CarrierMapper carrierMapper;

    @BeforeEach
    public void setUp() {
        carrierMapper = new CarrierMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(carrierMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(carrierMapper.fromId(null)).isNull();
    }
}
