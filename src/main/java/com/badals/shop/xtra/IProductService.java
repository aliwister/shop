package com.badals.shop.xtra;

import com.badals.shop.service.dto.ProductDTO;

public interface IProductService {
    ProductDTO lookup(String sku);
}
