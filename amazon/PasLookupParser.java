package com.badals.shop.vendor.amazon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.badals.shop.entity.ProductBody;
import com.badals.shop.payload.BrowseNode;
import com.badals.shop.payload.Dimensions;
import com.badals.shop.payload.Offer;
import com.badals.shop.payload.Price;
import com.badals.shop.vendor.amazon.pas.mappings.BrowseNodeNode;
import com.badals.shop.vendor.amazon.pas.mappings.BrowseNodesNode;
import com.badals.shop.vendor.amazon.pas.mappings.DimensionsNode;
import com.badals.shop.vendor.amazon.pas.mappings.ImageSetNode;
import com.badals.shop.vendor.amazon.pas.mappings.ItemNode;
import com.badals.shop.vendor.amazon.pas.mappings.OfferListingNode;
import com.badals.shop.vendor.amazon.pas.mappings.PriceNode;
import com.badals.shop.vendor.amazon.pas.mappings.SimilarProductNode;
import com.badals.shop.vendor.amazon.pas.mappings.VariationAttributeNode;

public class PasLookupParser {
   public static final String PARENT = "PARENT";
   public static final String CHILD = "CHILD";
   public static final String SIMPLE = "SIMPLE";
   

   public static ProductBody parseProduct(ItemNode i) {
      
      boolean isChild = (i.getParentAsin() != null && !i.getParentAsin().equals(i.getAsin()));
      boolean isParent = (i.getParentAsin() != null && i.getParentAsin().equals(i.getAsin()));
    
      
      // Create Product
      CRC32 checksum = new CRC32();
      checksum.update(i.getAsin().getBytes());
      System.out.println(ReflectionToStringBuilder.toString(i));
      
      ProductBody p = new ProductBody()
         .setId(checksum.getValue())
         .setActive(true)
         
         .setAsin(i.getAsin())
         .setLink(i.getDetailPageURL())
         //.setImageUrl(i.getLargeImage().getURL())
         
         .setBinding(i.getItemAttributes().getBinding())
         .setBrand(i.getItemAttributes().getBrand())
         .setEan(i.getItemAttributes().getEan())
         .setFeatures(i.getItemAttributes().getFeatures())
         .setItemDimensions(parseDimensions(i.getItemAttributes().getItemDimensions()))
         
         //.setAdultProduct(i.getItemAttributes().getIsAdultProduct().equals("True"))
         //.setMemorabilia(i.getItemAttributes().getIsMemorabilia().equals("True"))
         .setLabel(i.getItemAttributes().getLabel())
         .setListPrice(parsePrice(i.getItemAttributes().getListPrice()))
         .setManufacturer(i.getItemAttributes().getManufacturer())
         .setModel(i.getItemAttributes().getModel())
      
         .setPackageDimensions(parseDimensions(i.getItemAttributes().getPackageDimensions()))
         .setPartNumber(i.getItemAttributes().getPartNumber())
         .setProductGroup(i.getItemAttributes().getProductGroup())
         .setProductType(i.getItemAttributes().getProductTypeName())
         .setPublisher(i.getItemAttributes().getPublisher())
         .setReleaseDate(i.getItemAttributes().getReleaseDate())
         
         .setStudio(i.getItemAttributes().getStudio())
         .setTitle(i.getItemAttributes().getTitle())
         .setUpc(i.getItemAttributes().getUpc())
         .setUpdated(Calendar.getInstance().getTime())
         //.setCreated(Calendar.getInstance().getTime())
         .setStore("Amazon.com");
         
      if(isParent)
         p.setType(PARENT);
      else if(isChild)
         p.setType(CHILD);
      else
         p.setType(SIMPLE);

      // Images
      List<String> images = new ArrayList<>();
            
      if(i.getImageSets() != null) {
         for(ImageSetNode set : i.getImageSets()) {
            /*Image img = new Image()
                  .setTiny(set.getTinyImage().getURL())
                  .setSmall(set.getSmallImage().getURL())
                  .setMedium(set.getMediumImage().getURL())
                  .setLarge(set.getLargeImage().getURL())
                  .setPrimary(set.getCategory().equals("primary"));*/
            if(set.getCategory().equals("primary"))
               images.add(0, set.getLargeImage().getURL());
            else
               images.add(set.getLargeImage().getURL());
         }
         
         p.setImage_sets(images);
      }
      // Offer
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
         if(i.getVariationAttributes() != null) {
            
            Map<String, String> map = i.getVariationAttributes().stream().collect(
                  Collectors.toMap(VariationAttributeNode::getName, VariationAttributeNode::getValue));

            p.setVariationAttributes(map);
         }
      }
      
      return p;
     
      
      
      // Process availability
   }

   
   


   private static Price parsePrice(PriceNode price) {
      // TODO Auto-generated method stub
      if(price == null)
         return null;
      
      Double dPrice = Double.parseDouble(price.getAmount()) * .01;
      
      return new Price().setAmount(dPrice)
            .setCurrencyCode(price.getCurrencyCode());
   }
   
   private static Dimensions parseDimensions(DimensionsNode itemDimensions) {
      // TODO Auto-generated method stub
      if (itemDimensions == null)
         return null;
      
      // Dimensions
      String length = itemDimensions.getLength();
      String width  = itemDimensions.getWidth();
      String height = itemDimensions.getHeight();
      String weight = itemDimensions.getWeight();
      
      Dimensions p = new Dimensions();
      
      if(length != null)
         p.setLength(Double.parseDouble(length)*.01);
      
      if(width != null)
         p.setWidth(Double.parseDouble(width)*.01);
      
      if(height != null)
         p.setHeight(Double.parseDouble(height)*.01);
      
      if(weight != null)
         p.setWeight(Double.parseDouble(weight)*.01);
    
      return p;
   }
   
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
      
   }
}
