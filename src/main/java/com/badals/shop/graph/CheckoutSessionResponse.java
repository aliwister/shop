package com.badals.shop.graph;

import lombok.Data;
import java.io.Serializable;

@Data
public class CheckoutSessionResponse extends MutationResponse implements Serializable {
    String secureKey;
}
