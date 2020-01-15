package com.badals.shop.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ProductLangMapperTest {

    private ProductLangMapper productLangMapper;

    @BeforeEach
    public void setUp() {
        productLangMapper = new ProductLangMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(productLangMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(productLangMapper.fromId(null)).isNull();
    }
}
