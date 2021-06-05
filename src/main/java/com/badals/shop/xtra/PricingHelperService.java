package com.badals.shop.xtra;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.repository.ProductOverrideRepository;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.service.SlugService;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.PasItemNode;
import com.badals.shop.xtra.amazon.PasUtility;
import com.badals.shop.xtra.amazon.PricingException;
import com.badals.shop.xtra.amazon.mws.MwsItemNode;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.mws.MwsLookupParser;
import com.badals.shop.xtra.amazon.paapi5.PasLookupParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;

@Service
public class PricingHelperService {

   public static final long DEFAULT_WINDOW = 14400;
   private final ProductRepository productRepo;
   private final ProductOverrideRepository productOverrideRepository;

   private final MwsLookup mwsLookup;
   private final SlugService slugService;

   public PricingHelperService(ProductRepository productRepo, ProductOverrideRepository productOverrideRepository, MwsLookup mwsLookup, SlugService slugService) {
      this.productRepo = productRepo;
      this.productOverrideRepository = productOverrideRepository;
      this.mwsLookup = mwsLookup;
      this.slugService = slugService;
   }

   public List<ProductOverride> findOverrides(String asin, String parent) {
      if (parent == null)
         return productOverrideRepository.findBySku(asin);
      return productOverrideRepository.findBySkuIn(Arrays.asList(new String[]{asin, parent}));
   }

   public Product initProduct(Product product, PasItemNode item, boolean isParent, List<ProductOverride> overrides) {
      BigDecimal currentWeight = product.getWeight();
      product = (Product) PasLookupParser.parseProduct(product, item, isParent, overrides, 1L, "", "");

      product.setRating(item.getStarRating());

      if(product.getWeight() == null)
         product.setWeight(currentWeight);
      if((product.getWeight() == null || product.getWeight().doubleValue() < PasUtility.MINWEIGHT) && !isParent) {
         BigDecimal weight = productRepo.lookupWeight(product.getSku());
         product.setWeight(weight);
      }
      ProductLang lang = getLang(product);

      lang = (ProductLang) PasLookupParser.parseProductI18n(lang, item, "en");
      lang.setAttributes(item.parseAttributes());
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
         System.out.println(e.getMessage());
      } catch (NoOfferException e) {
         //e.printStackTrace();
         System.out.println(e.getMessage());
      }
      product.setStub(false);
      return product;
   }

   ProductLang getLang(Product product) {
      if(product.getProductLangs() == null)
         return new ProductLang();
      return product.getProductLangs().stream().findFirst().orElse(new ProductLang());
   }


   public MerchantStock getMerchantStock(Product product) {
      if(product.getMerchantStock() == null)
         return new MerchantStock();

      return product.getMerchantStock().stream().findFirst().orElse(new MerchantStock());
   }

   public Product setMerchantStock(Product product, MerchantStock stock, BigDecimal quantity) {
      if(stock.getId() == null) {
         stock.setMerchantId(1L);
         product.addMerchantStock(stock.link("amazon.com/dp/"+product.getSku()));
      }
      long window = DEFAULT_WINDOW;
      if ( product.getExpires() != null)
         window = Math.abs(Duration.between(product.getUpdated(), product.getExpires()).getSeconds());

      if ( product.getPrice() != null ) {
         double diff = product.getPrice().subtract(stock.getPrice()).abs().doubleValue();
         BigDecimal bPercent = BigDecimal.ONE.subtract(product.getPrice().divide(stock.getPrice(),8, RoundingMode.HALF_EVEN).subtract(BigDecimal.ONE).abs());
         double percent = bPercent.doubleValue();

         if (diff < .6 || percent < .06)
            window *= 1 + (1 - percent);
         else
            window = BigDecimal.valueOf(window).divide(bPercent, 8, RoundingMode.HALF_EVEN).longValue();
      }

      product.setExpires(Instant.now().plusSeconds(window));
      product.setPrice(new Price(stock.getPrice(), "OMR"));

      return product;
   }


   public Product priceMws(Product p, List<ProductOverride> overrides) throws NoOfferException {

      if (p.getWeight() == null || p.getWeight().doubleValue() < PasUtility.MINWEIGHT) return p;
      MwsItemNode n = mwsLookup.fetch(p.getSku());
      Product product = p;
      try {
         product = setMerchantStock(p, MwsLookupParser.parseStock(getMerchantStock(p),n, p.getWeight(), overrides), BigDecimal.valueOf(99L));
         product.inStock(true);
      } catch (PricingException e) {
         product.setPrice((BigDecimal) null);
         //e.printStackTrace();
      } catch (NoOfferException e) {
         //product = setMerchantStock(p, getMerchantStock(p),BigDecimal.ZERO);
         product.inStock(false);
      }
      return product;
   }

   public Product initStub(String key, List<Attribute> value, Long merchantId) {
      Product p = new Product();
      p.setVariationType(VariationType.CHILD);
      Long ref = slugService.generateRef(key, merchantId);
      p.slug(String.valueOf(ref)).ref(ref).merchantId(merchantId).active(true).sku(key).stub(true).inStock(true).title("stub");
      p.setVariationAttributes(value);
      p.setMerchantId(merchantId);
      return p;
   }

   public Product updateParentFromChildQuery(Product p, String key, Long merchantId) {
      p.setVariationType(VariationType.PARENT);
      Long ref = slugService.generateRef(key, merchantId);
      p.slug(String.valueOf(ref)).ref(ref).merchantId(merchantId).active(true).sku(key).stub(false).inStock(true).title("parent");
      p.setMerchantId(merchantId);
      return p;
   }

   public void resetDescriptions(Product product) {
      product.getProductLangs().stream().forEach(x->{
              x.setDescription(null);
              x.setFeatures(null);
      });
   }
}
