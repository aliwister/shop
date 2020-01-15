package com.badals.shop.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.shop.web.rest.TestUtil;

public class ProductLangTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductLang.class);
        ProductLang productLang1 = new ProductLang();
        productLang1.setId(1L);
        ProductLang productLang2 = new ProductLang();
        productLang2.setId(productLang1.getId());
        assertThat(productLang1).isEqualTo(productLang2);
        productLang2.setId(2L);
        assertThat(productLang1).isNotEqualTo(productLang2);
        productLang1.setId(null);
        assertThat(productLang1).isNotEqualTo(productLang2);
    }
}
