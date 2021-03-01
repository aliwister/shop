package com.badals.shop.service.query;

import com.badals.shop.domain.pojo.MerchantProductResponse;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.HashtagDTO;
import com.badals.shop.service.pojo.PartnerProduct;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PartnerQuery extends ShopQuery implements GraphQLQueryResolver {

   private final PartnerService partnerService;
   private final HashtagService hashtagService;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(PartnerQuery.class);

   private final UserService userService;

   public PartnerQuery(PartnerService partnerService, HashtagService hashtagService, CategoryService categoryService, UserService userService) {
      this.partnerService = partnerService;
      this.hashtagService = hashtagService;
      this.categoryService = categoryService;
      this.userService = userService;
   }

   //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    public PartnerProduct partnerProduct(Long id) {
       Long mId = 1L;//TenantContext.getCurrentMerchantId();
       return partnerService.getPartnerProduct(id, mId);
    }

   public MerchantProductResponse partnerProducts(String search, Integer limit, Integer offset, Boolean active) {
      Long mId = 1L;//TenantContext.getCurrentMerchantId();
      return partnerService.findAllByMerchant(mId, search, limit, offset, active);
   }

   public VariationOption variationOptions(String name) {
      if (name.equals("size")) {
         return new VariationOption("size", "Size", new ArrayList<String>(){{add("Small"); add("Medium"); add("Large");}});
      }
      else if (name.equals("color")) {
         return new VariationOption("color", "Color", new ArrayList<String>(){{add("Blue"); add("Red"); add("Yellow");}});
      }
      else if (name.equals("packsize")) {
         return new VariationOption("packsize", "Pack Size", new ArrayList<String>(){{add("Single"); add("Bag"); add("Big Box");}});
      }
      return null;
   }
   public List<VariationOption> variations() {
      return new ArrayList<VariationOption>(){{
         add(new VariationOption("size", "Size", new ArrayList<String>(){{add("Small"); add("Medium"); add("Large");}}));
         add(new VariationOption("color", "Color", new ArrayList<String>(){{add("Blue"); add("Red"); add("Yellow");}}));
         add(new VariationOption("packsize", "Pack Size", new ArrayList<String>(){{add("Single"); add("Bag"); add("Big Box");}}));
      }};
   }
   public List<HashtagDTO> hashtagList() {
      return hashtagService.findAll();
   }



   //partnerOrders(state: [OrderState], offset: Int, limit: Int, searchText: String): OrderResponse
   //partnerOrder(id: ID): Order

}

