package com.badals.shop.domain;

import com.badals.shop.domain.tenant.TenantProduct;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.shop.web.rest.TestUtil;

public class ProductTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantProduct.class);
        TenantProduct product1 = new TenantProduct();
        product1.setId(1L);
        TenantProduct product2 = new TenantProduct();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(2L);
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }
}
