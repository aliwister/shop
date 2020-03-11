package com.badals.shop.xtra.amazon;

import com.amazon.paapi5.v1.*;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.xtra.IMerchantProduct;
import com.badals.shop.xtra.IProductLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.CRC32;


public class PasLookupParser {

    private static final Logger log = LoggerFactory.getLogger(PasService.class);

    public static <T> T opt(Supplier<T> statement) {
        try {
            return statement.get();
        } catch (NullPointerException exc) {
            return null;
        }
    }

    public static IMerchantProduct parseProduct(IMerchantProduct p, Item i, boolean isParent) {

        //boolean isChild = (i.getParentASIN() != null && !i.getParentASIN().equals(i.getASIN()));
       // boolean isParent = (i.getParentASIN() != null && i.getParentASIN().equals(i.getASIN()));

        String image = null;
        image = opt(() -> i.getImages().getPrimary().getLarge().getURL());
        if (image == null)
            image = opt(() -> i.getImages().getVariants().get(0).getLarge().getURL());

        log.info(i.getASIN());
        log.info(Arrays.toString(i.getASIN().getBytes()));
        CRC32 checksum = new CRC32();
        checksum.update(i.getASIN().getBytes());
        long ref = checksum.getValue();

        log.info(String.valueOf(ref));

        // Create Product
        p.ref(ref).slug(String.valueOf(ref))
            .active(true)
            .sku(i.getASIN())
            .url(i.getDetailPageURL())
            //.setImageUrl(i.getLargeImage().getURL())

            //.setBinding(i.getItemAttributes().getBinding())
            .brand(opt(() ->i.getItemInfo().getByLineInfo().getBrand().getDisplayValue()))
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
            //.group(i.getItemAttributes().getProductGroup())
            //.setProductType(i.getItemAttributes().getProductTypeName())
            //.setPublisher(i.getItemAttributes().getPublisher())
            //.releaseDate(i.getItemAttributes().getReleaseDate())

            //.setStudio(i.getItemAttributes().getStudio())
            .image(image)
            .title(i.getItemInfo().getTitle().getDisplayValue())
            .upc(opt(() -> i.getItemInfo().getExternalIds().getUpCs().getDisplayValues().get(0)))
            .weight(opt(() -> i.getItemInfo().getProductInfo().getItemDimensions().getWeight().getDisplayValue()));
        //.updated(Calendar.getInstance().getTime());
        //.setCreated(Calendar.getInstance().getTime())
        //.setStore("Amazon.com");


        // Images
        List<Gallery> images = new ArrayList<>();

        if (i.getImages().getVariants() != null) {
            for (ImageType set : i.getImages().getVariants()) {
                images.add(new Gallery(set.getLarge().getURL()));
            }
            p.gallery(images);
        }
        // Offer
        if (!isParent) {

        }
        /*else {
            if (i.getVariations() != null) {
                p.setVariationDimensions(i.getVariations().variationDimensions);
            }
        }*/

        return p;


        // Process availability
    }

    public static IProductLang parseProductI18n(IProductLang p, Item i) {
        //if (i.getEditorialReviews() != null && !i.getEditorialReviews().isEmpty())
        //    p.setDescription(i.getEditorialReviews().get(0).getContent());
        p.setFeatures(opt(() ->i.getItemInfo().getFeatures().getDisplayValues()));
        p.setTitle(i.getItemInfo().getTitle().getDisplayValue());
        p.setModel(opt(() -> i.getItemInfo().getManufactureInfo().getModel().getDisplayValue()));
        p.setLang("en");
        return p;
    }


   /*private static Price parsePrice(PriceNode price) {
      // TODO Auto-generated method stub
      if(price == null)
         return null;

      BigDecimal dPrice = BigDecimal.valueOf(Double.parseDouble(price.getAmount())).movePointRight(2);

      return new Price(dPrice, price.getCurrencyCode());
   }*/


    private static final double USD2OMR = .386;
    private static final double LB2KG = 0.453592;
    private static final double OMRPERKG = 3.5;

    public static MerchantStock parseStock(MerchantStock stock, Item item) throws PricingException, NoOfferException {
        OfferListing offer = opt(() ->item.getOffers().getListings().get(0));
        if (offer == null)
            throw new NoOfferException("Offers is null");

        BigDecimal cost = offer.getPrice().getAmount();
        boolean isPrime = offer.getDeliveryInfo().isIsPrimeEligible();
        boolean isFulfilledByAmazon = offer.getDeliveryInfo().isIsAmazonFulfilled();
        boolean availability = offer.getAvailability().getType().equals("Now");

        double margin = 5, risk = 2, fixed = 1.1;

        BigDecimal weight = opt(() -> item.getItemInfo().getProductInfo().getItemDimensions().getWeight().getDisplayValue());
        if (weight == null)
            throw new PricingException("Unable to calculate price");

        double dWeight = weight.doubleValue()*LB2KG;
        if (dWeight < .0001) {
            //throw new Exception("Unable to caculate the price [weight=0]");
            fixed = 10000000;
        }
        if (dWeight < .05)
            dWeight += .4;

        dWeight = Math.max(dWeight, .3);
        double dCost = cost.doubleValue();
        double insurance = Math.log(dCost/dWeight)* Math.log10(dCost/dWeight) * Math.max(Math.log10(Math.sqrt(dCost)),1);
        insurance = .1*Math.max(insurance, 0);
        insurance = Math.min(insurance, 10);
        insurance = Math.min(insurance, 2+dCost);
        insurance = Math.max(insurance, .02*dCost);

        double localShipping = 0;

        if(!isPrime)
            insurance += 1+.15*dWeight;

        if(isPrime & dCost < 11 & dWeight < .5)
            insurance = -1;

        double c_add = (double) (margin + risk) * dCost *.01 + localShipping;
        double w_add = (double) OMRPERKG * dWeight ;

        //if($isDirect) 		$w_add = 0 ;
        //if(!isInsurance) 	$insurance = 0;
        double dPrice = (dCost + c_add + insurance  ) * USD2OMR + fixed + w_add;
        dPrice = Math.round(dPrice*10.0)/10.0;

        BigDecimal price = BigDecimal.valueOf(dPrice);
        //BigInteger availability = BigInteger.TEN;

        return new MerchantStock().store("Amazon.com").quantity(BigDecimal.valueOf(99)).cost(cost).availability(10).allow_backorder(true).price(price).link(item.getDetailPageURL()).location("USA");
    }

    public static void parseDimensions(Product p, GetVariationsResponse variationsResponse) {
        List<VariationDimension> dims = null;
        dims = opt(() -> variationsResponse.getVariationsResult().getVariationSummary().getVariationDimensions());
        List<VariationOption> options = new ArrayList<>();
        for(VariationDimension dim: dims) {
            //List<Attribute> attributes = i.getVariationAttributes().stream().map(v -> new Attribute(v.getName(), v.getValue()))
            //        .collect(Collectors.toList());
            //for (dim.g)
            options.add(new VariationOption(dim.getDisplayName(),dim.getName(), dim.getValues().stream().collect(Collectors.toList())));

            //p.setVariationAttributes(attributes);
        }
        p.setVariationOptions(options);
    }

    public static void parseVariationAttributes(Product child, Item childItem) {
        List<Attribute> attributes = new ArrayList<>();
        for(VariationAttribute a : childItem.getVariationAttributes()) {
            Attribute v= new Attribute (a.getName(), a.getValue());
            attributes.add(v);
        }
        child.setVariationAttributes(attributes);
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
