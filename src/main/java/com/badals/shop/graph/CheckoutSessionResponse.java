package com.badals.shop.graph;

import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.pojo.CheckoutSession;
import lombok.Data;

import java.io.Serializable;

@Data
public class CheckoutSessionResponse extends MutationResponse implements Serializable {
    String secureKey;
}
