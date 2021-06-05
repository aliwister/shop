package com.badals.shop.xtra.ebay;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.repository.ProductOverrideRepository;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.PasItemNode;
import com.badals.shop.xtra.amazon.PricingException;
import com.badals.shop.xtra.amazon.paapi5.PasLookupParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class EbayService {

   private final ProductRepository productRepo;
   private final EbayLookup ebayLookup;
   private final ProductOverrideRepository productOverrideRepository;

   public EbayService(ProductRepository productRepo, EbayLookup ebayLookup, ProductOverrideRepository productOverrideRepository) {
      this.productRepo = productRepo;
      this.ebayLookup = ebayLookup;
      this.productOverrideRepository = productOverrideRepository;
   }
   private List<ProductOverride> findOverrides(String asin, String parent) {
      if (parent == null)
         return productOverrideRepository.findBySku(asin);
      return productOverrideRepository.findBySkuIn(Arrays.asList(new String[]{asin, parent}));
   }

   @Transactional
   public Product lookup(String id, boolean isParent) throws NoOfferException, ProductNotFoundException, PricingException {
      List<ProductOverride> overrides = findOverrides(id, null);
      Product product = productRepo.findBySkuJoinChildren(id, 2L).orElse(new Product());
      PasItemNode item = ebayLookup.lookup(id);
      product = initProduct(product, item, false, overrides);
      product.setVariationType(item.getVariationType());
      product.setUrl(item.getUrl());
      product = productRepo.save(product);


      return product;
   }

   ProductLang getLang(Product product) {
      if(product.getProductLangs() == null)
         return new ProductLang();
      return product.getProductLangs().stream().findFirst().orElse(new ProductLang());
   }

   Product initProduct(Product product, PasItemNode item, boolean isParent, List<ProductOverride> overrides) {
      product = (Product) PasLookupParser.parseProduct(product, item, isParent, overrides,2L,"992", "EB");
      if((product.getWeight() == null || product.getWeight().doubleValue() < .001) && !isParent) {
         BigDecimal weight = productRepo.lookupWeight(product.getSku());
         product.setWeight(weight);
      }
      ProductLang lang = getLang(product);

      lang = (ProductLang) PasLookupParser.parseProductI18n(lang, item, "en");

      if(lang.getId() == null) {
         product.addProductLang(lang);
      }
      //if(item.get() == null || (!item.isSuperSaver() && !item.isPrime()))
      //  return product;
      if(isParent)
         return product;

      MerchantStock stock = this.getMerchantStock(product);
      try {
         product = setMerchantStock(product, PasLookupParser.parseStock(product, stock, item, overrides),BigDecimal.valueOf(99L));
      } catch (PricingException e) {
         //e.printStackTrace();//@Todo set stock quantity to 0
      } catch (NoOfferException e) {
         //e.printStackTrace();
      }
      return product;
   }

   Product setMerchantStock(Product product, MerchantStock stock, BigDecimal quantity) {
      if(stock.getId() == null) {
         stock.setMerchantId(1L);
         product.addMerchantStock(stock.link("amazon.com/dp/"+product.getSku()));
      }
      product.setPrice(new Price(stock.getPrice(), "OMR"));

      return product;
   }

   MerchantStock getMerchantStock(Product product) {
      if(product.getMerchantStock() == null)
         return new MerchantStock();

      return product.getMerchantStock().stream().findFirst().orElse(new MerchantStock());
   }
}
