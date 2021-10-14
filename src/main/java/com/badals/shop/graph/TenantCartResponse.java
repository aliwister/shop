package com.badals.shop.graph;

import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.service.dto.CartDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class TenantCartResponse extends MutationResponse implements Serializable {
    CheckoutCart cart;
    int total;
    boolean hasMore;
}
