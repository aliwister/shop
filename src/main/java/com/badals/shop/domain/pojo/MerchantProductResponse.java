package com.badals.shop.domain.pojo;

import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.pojo.AddProductDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MerchantProductResponse implements Serializable {
    List<AddProductDTO> items;
    int total;
    boolean hasMore;
}
