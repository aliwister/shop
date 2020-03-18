package com.badals.shop.xtra.amazon.paapi4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.zip.CRC32;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.xtra.IMerchantProduct;
import com.badals.shop.xtra.IProductLang;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.PasUtility;
import com.badals.shop.xtra.amazon.PricingException;


public class Pas4LookupParser {
   public static <T> T opt(Supplier<T> statement) {
      try {
         return statement.get();
      } catch (NullPointerException exc) {
         return null;
      }
   }

   public static IProductLang parseProductI18n(IProductLang p, PasItemNode i) {
      //if (i.getEditorialReviews() != null && !i.getEditorialReviews().isEmpty())
      //    p.setDescription(i.getEditorialReviews().get(0).getContent());
      p.setFeatures(opt(() ->i.getFeatures()));
      p.setTitle(i.getTitle());
      p.setModel(i.getModel());
      p.setLang("en");
      p.setDescription(i.getEditorial());
      return p;
   }

   public static IMerchantProduct parseProduct(IMerchantProduct p, PasItemNode i, boolean isParent) {
      // Create Product

      //Reset price
      p.setPrice(null);

      CRC32 checksum = new CRC32();
      checksum.update(i.getAsin().getBytes());
      long ref = checksum.getValue();
      p.ref(ref).slug(String.valueOf(ref))
         .active(true)
         .sku(i.getAsin())
         .url(i.getUrl())
         //.setImageUrl(i.getLargeImage().getURL())
         .image(i.getImage())
         //.setBinding(i.getItemAttributes().getBinding())
         .brand(i.getBrand())
         .weight(parseWeight(i))

         //.setEan(i.getItemAttributes().getEan())
         //.features(i.getItemAttributes().getFeatures())
         //.setItemDimensions(parseDimensions(i.getItemAttributes().getItemDimensions()))
         
         //.setAdultProduct(i.getItemAttributes().getIsAdultProduct().equals("True"))
         //.setMemorabilia(i.getItemAttributes().getIsMemorabilia().equals("True"))
         //.setLabel(i.getItemAttributes().getLabel())
         //.setListPrice(parsePrice(i.getItemAttributes().getListPrice()))
         //.setManufacturer(i.getItemAttributes().getManufacturer())
         //.model(i.getItemAttributes().getModel())
      
         //.setPackageDimensions(parseDimensions(i.getItemAttributes().getPackageDimensions()))
        // .setPartNumber(i.getItemAttributes().getPartNumber())
        // .setProductGroup(i.getItemAttributes().getProductGroup())
         //.setProductType(i.getItemAttributes().getProductTypeName())
         //.setPublisher(i.getItemAttributes().getPublisher())
        // .setReleaseDate(i.getItemAttributes().getReleaseDate())

         //.setStudio(i.getItemAttributes().getStudio())
         .title(i.getTitle())
         .upc(i.getUpc());
         //.setUpdated(Calendar.getInstance().getTime())
         //.setCreated(Calendar.getInstance().getTime())
         //.setStore("Amazon.com");

      // Images
      List<Gallery> images = new ArrayList<>();
            
      if(i.getGallery() != null) {
         for(String gUrl : i.getGallery()) {
            /*Image img = new Image()
                  .setTiny(set.getTinyImage().getURL())
                  .setSmall(set.getSmallImage().getURL())
                  .setMedium(set.getMediumImage().getURL())
                  .setLarge(set.getLargeImage().getURL())
                  .setPrimary(set.getCategory().equals("primary"));*/
            //if(set.getCategory().equals("primary"))
             // p.image(set.getLargeImage().getURL());
           // else
               images.add(new Gallery(gUrl));
         }
         
         p.gallery(images);
      }
      // Offer
      //
      /*
      if(!isParent) {
         
         
         if (i.getOffers() != null && i.getOffers().getTotalOffers() != null && i.getOffers().getTotalOffers().equals("1")) {
            OfferListingNode offerNode = i.getOffers().getOffer().getOfferListing();
            if (offerNode != null) {
               Offer o = new Offer()
                     .setPrice(parsePrice(offerNode.getPrice()))
                     .setAvailability(Integer.parseInt(offerNode.getAvailabilityAttributesNode().getMaximumHours()))
                     .setPrime(offerNode.getIsEligibleForPrime().equals("True"))
                     .setSuperSaver(offerNode.getIsEligibleForSuperSaverShipping().equals("True"));
               
               p.setOffer(o);
            }
         }
      }
      else {
         if(i.getVariations() != null) {
            p.setVariationDimensions(i.getVariations().variationDimensions);
           
            
            Map<String, Set<String>> variationOptions = new HashMap<String, Set<String>>();
            p.getVariationDimensions().forEach((k) -> variationOptions.put(k, new LinkedHashSet<String>())); 
            p.setVariations(new HashMap<Long, Map<String, String>>());
            p.setVariationOptions(variationOptions);
            
         }
      }
      
      
      if(!isChild) {
         // Similarities
         
         if(i.getSimilarProducts() != null) {
            List<String> similars = i.getSimilarProducts().stream().map(SimilarProductNode::getASIN).collect(Collectors.toList());
            if(similars != null && similars.size() > 0)
               p.setSimilarProducts(similars);
         }
         
         if (i.getEditorialReviews() != null && !i.getEditorialReviews().isEmpty())
            p.setDescription(i.getEditorialReviews().get(0).getContent());       
         
         if(i.getBrowseNodes() != null && !i.getBrowseNodes().browseNode.isEmpty()) 
            p.setVendor_crumbs(parseCrumbs(i.getBrowseNodes()));
         
         
      }
      else {

      }
      */
      return p;
      // Process availability
   }

   private static BigDecimal parseWeight(PasItemNode i) {
      String weight = (i.getPackageWeight() == null)?i.getItemWeight():i.getPackageWeight();
      if(weight != null)
         return BigDecimal.valueOf(Double.parseDouble(weight)*.01);
      return null;
   }

   public static MerchantStock parseStock(MerchantStock stock, PasItemNode i) throws PricingException, NoOfferException {
      // @Todo catch options with no offers

      String sCost = i.getCost();



      Double dCost = Double.parseDouble(sCost) * .01;
      String sWeight = i.getPackageWeight();
      if (sWeight == null)
         sWeight = i.getItemWeight();

      if(sWeight == null)
         throw new NoOfferException("Weight is null");

      Double dWeight = Double.parseDouble(sWeight) * .01;

      boolean isPrime = i.isPrime();
      boolean isFulfilledByAmazon = i.isSuperSaver();
      int availability = Integer.parseInt(i.getMaxHours());

      double margin = 5, risk = 2, fixed = 1.1;
      double localShipping = 0;

      BigDecimal price = PasUtility.calculatePrice(BigDecimal.valueOf(dCost), BigDecimal.valueOf(dWeight), localShipping, margin, risk, fixed, isPrime, isFulfilledByAmazon);
      return new MerchantStock().store("Amazon.com").quantity(BigDecimal.valueOf(99)).cost(BigDecimal.valueOf(dCost)).availability(5+availability/24).allow_backorder(true).price(price).link(i.getUrl()).location("USA");
   }

   public static void parseVariationAttributes(Product child, PasItemNode childItem) {
      List<Attribute> attributes = new ArrayList<>();
      for(Attribute a : childItem.getVariationAttributes()) {
         //Attribute v= new Attribute (a.getName(), a.getValue());
         attributes.add(a);
      }
      child.setVariationAttributes(attributes);
   }


   

   /*
   public static List<BrowseNode> parseCrumbs(BrowseNodesNode browseNodes) {
      //return null;
      List<BrowseNode> nodes = new ArrayList<>();
      
      for (BrowseNodeNode browse : browseNodes.browseNode) {
         List<BrowseNode> temp = new ArrayList<>();
         temp.add(new BrowseNode(browse.getBrowseNodeId(), browse.getName()));
         
         
         BrowseNodeNode ancestor = null;
         
         if(browse.getAncestors() != null && browse.getAncestors().getBrowseNode() != null)
            ancestor = browse.getAncestors().getBrowseNode();
         
         while (ancestor != null) {
            temp.add(new BrowseNode(ancestor.getBrowseNodeId(), ancestor.getName()));
            
            
            if(ancestor.getAncestors() != null && ancestor.getAncestors().getBrowseNode() != null)
               ancestor = ancestor.getAncestors().getBrowseNode();
            else
               break;
         }
         
         Collections.reverse(temp);
         String num = temp.stream()
               .map(BrowseNode::getNumerical)
               .collect(Collectors.joining(" > "));
         
         String text = temp.stream()
               .map(BrowseNode::getTextual)
               .collect(Collectors.joining(" > "));
         
         nodes.add(new BrowseNode(num.trim(), text.trim()));       
     }
      
     return nodes;
      
   }*/
}
