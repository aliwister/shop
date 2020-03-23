package com.badals.shop.xtra;

import com.badals.shop.domain.Product;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.xtra.amazon.NoOfferException;

public interface IProductService {
    Product lookup(String sku, boolean isRedis, boolean isRebuild) throws NoOfferException;
}
