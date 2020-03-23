package com.badals.shop.xtra.amazon;

import com.amazon.paapi5.v1.*;
import com.badals.shop.domain.pojo.Attribute;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.badals.shop.service.util.AccessUtil.opt;

@Mapper(componentModel = "spring", uses = {})
public interface PasItemMapper {

   PasItemMapper INSTANCE = Mappers.getMapper(PasItemMapper.class);


   @Mapping(source = "ASIN", target = "asin")
   @Mapping(source = "parentASIN", target = "parentAsin")
   @Mapping(source = "itemInfo.byLineInfo.brand.displayValue", target = "brand")
   @Mapping(source = "itemInfo.byLineInfo.manufacturer.displayValue", target = "manufacturer")
   @Mapping(source = "detailPageURL", target = "url")
   @Mapping(source = "images.primary.large.URL", target = "image")
   @Mapping(source = "itemInfo.title.displayValue", target = "title")

   @Mapping(source = "itemInfo.productInfo.itemDimensions.weight.displayValue", target = "itemWeight")
   @Mapping(source = "itemInfo.manufactureInfo.model.displayValue", target="model")
   @Mapping(source = "itemInfo.features.displayValues", target="features")
   @Mapping(source = "itemInfo.productInfo.releaseDate.displayValue", target="releaseDate")
   @Mapping(source = "itemInfo.productInfo.color.displayValue", target="color")
   @Mapping(source = "itemInfo.productInfo.size.displayValue", target="size")
   @Mapping(source = "itemInfo.productInfo.unitCount.displayValue", target="unitCount")
   @Mapping(source = "itemInfo.productInfo.isAdultProduct.displayValue", target="adult")
   @Mapping(source = "itemInfo.classifications.productGroup.displayValue", target="productGroup")
   //@Mapping(source = "", target="starRating")
   @Mapping(target = "cost", ignore = true)
   @Mapping(target = "prime", ignore = true)
   @Mapping(target = "superSaver", ignore = true)
   @Mapping(target = "availabilityType", ignore = true)
   @Mapping(target = "availabilityMessage", ignore = true)
   PasItemNode itemToPasItemNode(Item item);

   @AfterMapping
   default void afterMapping(@MappingTarget PasItemNode target, Item source) {
      // UPC
      String upc = opt(() ->source.getItemInfo().getExternalIds().getUpCs().getDisplayValues().get(0));
      if (upc != null)
         target.setUpc(upc);

      // UPC
      String ean = opt(() ->source.getItemInfo().getExternalIds().getEaNs().getDisplayValues().get(0));
      if (ean != null)
         target.setEan(ean);

      // ISBN
      String isbn = opt(() ->source.getItemInfo().getExternalIds().getIsBNs().getDisplayValues().get(0));
      if (isbn != null)
         target.setIsbn(isbn);

      // Offer
      OfferListing offer = opt(() ->source.getOffers().getListings().get(0));
      if(offer != null && offer.getPrice() != null) {
         target.setCost(offer.getPrice().getAmount());
         target.setPrime(offer.getDeliveryInfo().isIsPrimeEligible());
         target.setSuperSaver(offer.getDeliveryInfo().isIsAmazonFulfilled());
         target.setFreeShipping(offer.getDeliveryInfo().isIsFreeShippingEligible());
         target.setShippingCharges(opt(() -> offer.getDeliveryInfo().getShippingCharges().get(0).getAmount()));
         target.setAvailabilityType(offer.getAvailability().getType());
         target.setAvailabilityMessage(offer.getAvailability().getMessage());
         target.setSavingsAmount(opt(() -> offer.getPrice().getSavings().getAmount()));
         target.setSavingsPercentage(opt(() -> offer.getPrice().getSavings().getPercentage()));
         target.setListPrice(opt(() -> offer.getSavingBasis().getAmount()));
      }
      // Variation
      if(source.getVariationAttributes() != null) {
         for(VariationAttribute a : source.getVariationAttributes()) {
            Attribute v= new Attribute (a.getName(), a.getValue());
            target.getVariationAttributes().add(v);
         }
      }
      // Gallery
      if(source.getImages().getVariants() != null) {
         for (ImageType set : source.getImages().getVariants()) {
            target.getGallery().add(set.getLarge().getURL());
         }
      }

      // Browsenodes

      BrowseNode browse = opt(() -> source.getBrowseNodeInfo().getBrowseNodes().get(0));

      if(browse == null)
         return;
      List<PasBrowseNode> temp = new ArrayList<>();
      temp.add(new PasBrowseNode(browse.getId(), browse.getDisplayName()));
      BrowseNodeAncestor ancestor = browse.getAncestor();

      while (ancestor != null) {
         if(!ancestor.getDisplayName().equals("Categories") && !ancestor.getDisplayName().equals("Shops"))
            temp.add(new PasBrowseNode(ancestor.getId(), ancestor.getDisplayName()));
         ancestor = ancestor.getAncestor();
      }

      Collections.reverse(temp);
      String browseNode = temp.stream()
        .map(PasBrowseNode::getDisplayName)
        .collect(Collectors.joining(" > "));
      target.setBrowseNode(browseNode);
      /*
      String n = "";
      String t = "";

      for(PasBrowseNode x : temp) {
         target.getCategories().add(x.getDisplayName());
         target.getNumCategories().add(x.getId());

         if(t.isEmpty()) {
            t = x.getDisplayName();
            n = x.getId();
         }
         else {
            t += " > " + x.getDisplayName();
            n += " > " + x.getId();
         }

         target.getHierarchialCategories().add(t);
         target.getNumHierarchialCategories().add(n);
      }*/
   }
}
