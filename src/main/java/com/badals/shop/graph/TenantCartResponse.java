package com.badals.shop.graph;

import com.badals.shop.domain.CheckoutCart;
import lombok.Data;

import java.io.Serializable;

@Data
public class TenantCartResponse extends MutationResponse implements Serializable {
    CheckoutCart cart;
    int total;
    boolean hasMore;
}
