package com.badals.shop.xtra.amazon.paapi5;

import com.amazon.paapi5.v1.*;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.enumeration.OverrideType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.xtra.IMerchantProduct;
import com.badals.shop.xtra.IProductLang;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas4Service;
import com.badals.shop.xtra.amazon.PasUtility;
import com.badals.shop.xtra.amazon.PricingException;
import com.badals.shop.xtra.amazon.paapi4.PasItemNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.CRC32;


public class PasLookupParser {

    private static final Logger log = LoggerFactory.getLogger(Pas4Service.class);

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
        //Reset price
        p.setPrice(null);
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

    public static MerchantStock parseStock(MerchantStock stock, Item item, List<ProductOverride> overrides) throws PricingException, NoOfferException {
        OfferListing offer = opt(() ->item.getOffers().getListings().get(0));
        if (offer == null)
            throw new NoOfferException("Offers is null");

        BigDecimal cost = opt(() ->offer.getPrice().getAmount()); //offer.getPrice().getAmount();
        if(cost == null)
            throw new NoOfferException("Cost is null");

        BigDecimal weight = opt(() -> item.getItemInfo().getProductInfo().getItemDimensions().getWeight().getDisplayValue());
        weight = calculateWeight(weight, getOverride(overrides, OverrideType.WEIGHT));

        if(weight == null)
            throw new NoOfferException("Weight is null");

        boolean isPrime = offer.getDeliveryInfo().isIsPrimeEligible();
        boolean isFulfilledByAmazon = offer.getDeliveryInfo().isIsAmazonFulfilled();
        boolean availability = offer.getAvailability().getType().equals("Now");

        double margin = 5, risk = 2, fixed = 1.1;
        double localShipping = 0;

        BigDecimal price = PasUtility.calculatePrice(cost, weight, localShipping, margin, risk, fixed, isPrime, isFulfilledByAmazon);
        price = calculatePrice(price, getOverride(overrides, OverrideType.PRICE));

        return new MerchantStock().store("Amazon.com").quantity(BigDecimal.valueOf(99)).cost(cost).availability(10).allow_backorder(true).price(price).link(item.getDetailPageURL()).location("USA");
    }

    private static BigDecimal calculateWeight(BigDecimal weight, ProductOverride override) {
        if (override == null) return weight;
        if(!override.isLazy()  ||  (override.isLazy() && weight == null))
            return new BigDecimal(override.getOverride());
        return weight;
    }

    private static BigDecimal calculatePrice(BigDecimal price, ProductOverride override) {
        if (override == null) return price;
        if(!override.isLazy()  ||  (override.isLazy() && price == null))
            return new BigDecimal(override.getOverride());
        return price;
    }

    private static ProductOverride getOverride(List<ProductOverride> overrides, OverrideType type) {
        return overrides.stream().filter(x -> x.getType() == type).findFirst().orElse(null);
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
