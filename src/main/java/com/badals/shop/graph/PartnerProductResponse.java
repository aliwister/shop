package com.badals.shop.graph;

import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.service.pojo.PartnerProduct;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PartnerProductResponse extends MutationResponse implements Serializable {
    List<PartnerProduct> items;
    int total;
    boolean hasMore;
}
