package com.badals.shop.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.shop.web.rest.TestUtil;

public class ProductLangDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductLangDTO.class);
        ProductLangDTO productLangDTO1 = new ProductLangDTO();
        productLangDTO1.setId(1L);
        ProductLangDTO productLangDTO2 = new ProductLangDTO();
        assertThat(productLangDTO1).isNotEqualTo(productLangDTO2);
        productLangDTO2.setId(productLangDTO1.getId());
        assertThat(productLangDTO1).isEqualTo(productLangDTO2);
        productLangDTO2.setId(2L);
        assertThat(productLangDTO1).isNotEqualTo(productLangDTO2);
        productLangDTO1.setId(null);
        assertThat(productLangDTO1).isNotEqualTo(productLangDTO2);
    }
}
