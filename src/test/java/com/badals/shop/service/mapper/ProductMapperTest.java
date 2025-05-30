package com.badals.shop.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ProductMapperTest {

    private TenantProductMapper productMapper;

    @BeforeEach
    public void setUp() {
        productMapper = new TenantProductMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(productMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(productMapper.fromId(null)).isNull();
    }
}
