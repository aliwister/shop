package com.badals.shop.xtra.amazon;

import com.badals.shop.domain.Product;
import com.badals.shop.domain.enumeration.ProductType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.xtra.IMerchantProduct;
import com.badals.shop.xtra.IProductLang;
import com.badals.shop.xtra.amazon.mappings.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

public class PasLookupParser {

    private static final Logger log = LoggerFactory.getLogger(PasService.class);

    public static IMerchantProduct parseProduct(IMerchantProduct p, ItemNode i) {

        boolean isChild = (i.getParentAsin() != null && !i.getParentAsin().equals(i.getAsin()));
        boolean isParent = (i.getParentAsin() != null && i.getParentAsin().equals(i.getAsin()));

        String image = null;
        if(i.getLargeImage() != null)
            image = i.getLargeImage().getURL();
        else if(i.getImageSets() != null && i.getImageSets().get(0).getLargeImage() != null)
            image = i.getImageSets().get(0).getLargeImage().getURL();

        log.info(i.getAsin());
        log.info(Arrays.toString(i.getAsin().getBytes()));
        CRC32 checksum = new CRC32();
        checksum.update(i.getAsin().getBytes());
        long ref = checksum.getValue();

        log.info(String.valueOf(ref));

        // Create Product
        p.ref(ref)
            .active(true)
            .sku(i.getAsin())
            .url(i.getDetailPageURL())
            //.setImageUrl(i.getLargeImage().getURL())

            //.setBinding(i.getItemAttributes().getBinding())
            .brand(i.getItemAttributes().getBrand())
            //.setEan(i.getItemAttributes().getEan())
            //.setFeatures(i.getItemAttributes().getFeatures())
            //.setItemDimensions(parseDimensions(i.getItemAttributes().getItemDimensions()))

            //.setAdultProduct(i.getItemAttributes().getIsAdultProduct().equals("True"))
            //.setMemorabilia(i.getItemAttributes().getIsMemorabilia().equals("True"))
            //.setLabel(i.getItemAttributes().getLabel())
            //.setListPrice(parsePrice(i.getItemAttributes().getListPrice()))
            //.setManufacturer(i.getItemAttributes().getManufacturer())
            //.setModel(i.getItemAttributes().getModel())

            //.setPackageDimensions(parseDimensions(i.getItemAttributes().getPackageDimensions()))
            //.setPartNumber(i.getItemAttributes().getPartNumber())
            .group(i.getItemAttributes().getProductGroup())
            //.setProductType(i.getItemAttributes().getProductTypeName())
            //.setPublisher(i.getItemAttributes().getPublisher())
            //.releaseDate(i.getItemAttributes().getReleaseDate())

            //.setStudio(i.getItemAttributes().getStudio())
            .image(image)
            .title(i.getItemAttributes().getTitle())
            .upc(i.getItemAttributes().getUpc());
        //.updated(Calendar.getInstance().getTime());
        //.setCreated(Calendar.getInstance().getTime())
        //.setStore("Amazon.com");

        if (isParent)
            p.type(ProductType.PARENT);
        else if (isChild)
            p.type(ProductType.CHILD);
        else
            p.type(ProductType.SIMPLE);

        // Images
        List<String> images = new ArrayList<>();

        if (i.getImageSets() != null) {
            for (ImageSetNode set : i.getImageSets()) {
            /*Image img = new Image()
                  .setTiny(set.getTinyImage().getURL())
                  .setSmall(set.getSmallImage().getURL())
                  .setMedium(set.getMediumImage().getURL())
                  .setLarge(set.getLargeImage().getURL())
                  .setPrimary(set.getCategory().equals("primary"));*/
                if (set.getCategory().equals("primary"))
                    images.add(0, set.getLargeImage().getURL());
                else
                    images.add(set.getLargeImage().getURL());
            }

            p.images(images);
        }
        // Offer
        if (!isParent) {


         /*if (i.getOffers() != null && i.getOffers().getTotalOffers() != null && i.getOffers().getTotalOffers().equals("1")) {
            OfferListingNode offerNode = i.getOffers().getOffer().getOfferListing();
            if (offerNode != null) {
               Offer o = new Offer()
                     .setPrice(parsePrice(offerNode.getPrice()))
                     .setAvailability(Integer.parseInt(offerNode.getAvailabilityAttributesNode().getMaximumHours()))
                     .setPrime(offerNode.getIsEligibleForPrime().equals("True"))
                     .setSuperSaver(offerNode.getIsEligibleForSuperSaverShipping().equals("True"));

               p.setOffer(o);
            }
         }*/
        } else {
            if (i.getVariations() != null) {
                p.setVariationDimensions(i.getVariations().variationDimensions);
            }
        }


        if (!isChild) {
            // Similarities

            if (i.getSimilarProducts() != null) {
                List<String> similars = i.getSimilarProducts().stream().map(SimilarProductNode::getASIN).collect(Collectors.toList());
                if (similars != null && similars.size() > 0)
                    p.setSimilarProducts(similars);
            }

            // if(i.getBrowseNodes() != null && !i.getBrowseNodes().browseNode.isEmpty())
            //    p.setVendor_crumbs(parseCrumbs(i.getBrowseNodes()));


        } else {
            if (i.getVariationAttributes() != null) {

                List<Attribute> attributes = i.getVariationAttributes().stream().map(v -> new Attribute(v.getName(), v.getValue()))
                    .collect(Collectors.toList());

                p.setVariationAttributes(attributes);
            }
        }

        return p;


        // Process availability
    }

    public static IProductLang parseProductI18n(IProductLang p, ItemNode i) {
        if (i.getEditorialReviews() != null && !i.getEditorialReviews().isEmpty())
            p.setDescription(i.getEditorialReviews().get(0).getContent());
        p.setFeatures(i.getItemAttributes().getFeatures());
        p.setTitle(i.getItemAttributes().getTitle());
        p.setModel(i.getItemAttributes().getModel());
        p.setLang("en");
        return p;
    }


   private static Price parsePrice(PriceNode price) {
      // TODO Auto-generated method stub
      if(price == null)
         return null;

      BigDecimal dPrice = BigDecimal.valueOf(Double.parseDouble(price.getAmount())).movePointRight(2);

      return new Price(dPrice, price.getCurrencyCode());
   }

   /*private static Dimensions parseDimensions(DimensionsNode itemDimensions) {
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

   }*/
}
