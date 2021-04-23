package com.badals.shop.graph;

import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.pojo.AddProductDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MerchantProductResponse extends MutationResponse implements Serializable {
    List<AddProductDTO> items;
    int total;
    boolean hasMore;
}
