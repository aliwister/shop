package com.badals.shop.graph;

import com.badals.shop.domain.tenant.Checkout;
import lombok.Data;

import java.io.Serializable;

@Data
public class TenantCartResponse extends MutationResponse implements Serializable {
    Checkout cart;
    int total;
    boolean hasMore;
}
