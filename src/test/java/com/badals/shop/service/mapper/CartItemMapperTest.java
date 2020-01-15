package com.badals.shop.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CartItemMapperTest {

    private CartItemMapper cartItemMapper;

    @BeforeEach
    public void setUp() {
        cartItemMapper = new CartItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(cartItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cartItemMapper.fromId(null)).isNull();
    }
}
