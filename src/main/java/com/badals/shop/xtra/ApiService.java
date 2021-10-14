package com.badals.shop.xtra;

import com.badals.shop.domain.Product;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.IncorrectDimensionsException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.PasItemNode;
import com.badals.shop.xtra.amazon.PricingException;

public interface ApiService {
   public Product callback(PasItemNode item) throws IncorrectDimensionsException, PricingException, NoOfferException, ProductNotFoundException;
}
