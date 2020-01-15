package com.badals.shop.xtra;

import com.badals.shop.domain.enumeration.ProductType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Price;

import java.util.List;
import java.util.Set;

public interface IMerchantProduct {
    IMerchantProduct ref(Long ref);

    IMerchantProduct active(Boolean b);

    IMerchantProduct sku(String asin);

    IMerchantProduct url(String detailPageURL);

    IMerchantProduct brand(String brand);

    IMerchantProduct group(String productGroup);

    IMerchantProduct image(String image);

    IMerchantProduct title(String title);

    IMerchantProduct upc(String upc);

    IMerchantProduct type(ProductType parent);

    IMerchantProduct images(List<String> images);

    void setPrice(Price price);

    void setVariationDimensions(List<String> variationDimensions);

    void setSimilarProducts(List<String> similars);

    void setVariationAttributes(List<Attribute> attributes);

    void setProductLangs(Set<IProductLang> i18n);

    List<String> getVariationDimensions();
}
