package com.badals.shop.xtra;

import com.badals.shop.domain.Product;
import com.badals.shop.service.dto.ProductDTO;

public interface IProductService {
    Product lookup(String sku, boolean isRedis, boolean isRebuild);
}
