package com.badals.shop.xtra;

import com.badals.shop.domain.enumeration.ProductGroup;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.domain.pojo.Price;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface IMerchantProduct {
    IMerchantProduct ref(Long ref);

    IMerchantProduct slug(String slug);

    IMerchantProduct active(Boolean b);

    IMerchantProduct sku(String asin);

    IMerchantProduct url(String detailPageURL);

    IMerchantProduct brand(String brand);

    IMerchantProduct group(ProductGroup productGroup);

    IMerchantProduct image(String image);

    IMerchantProduct title(String title);

    IMerchantProduct upc(String upc);

    IMerchantProduct variationType(VariationType type);

    IMerchantProduct gallery(List<Gallery> images);

    IMerchantProduct weight(BigDecimal weight);
    IMerchantProduct volumeWeight(BigDecimal volumeWeight);



    void setPrice(Price price);

    void setVariationDimensions(List<String> variationDimensions);

    void setSimilarProducts(List<String> similars);

    void setVariationAttributes(List<Attribute> attributes);

    void setProductLangs(Set<IProductLang> i18n);

    List<String> getVariationDimensions();

   IMerchantProduct merchantId(Long l);
}
