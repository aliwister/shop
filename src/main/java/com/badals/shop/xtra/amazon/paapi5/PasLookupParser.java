package com.badals.shop.xtra.amazon.paapi5;

import com.amazon.paapi5.v1.*;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.enumeration.OverrideType;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.xtra.IMerchantProduct;
import com.badals.shop.xtra.IProductLang;
import com.badals.shop.xtra.amazon.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.time.Period ;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

import static com.badals.shop.service.util.AccessUtil.opt;
import static java.time.LocalDate.MAX;


public class PasLookupParser {

    private static final Logger log = LoggerFactory.getLogger(PasLookupParser.class);

    public static IMerchantProduct parseProduct(IMerchantProduct p, PasItemNode i, boolean isParent, List<ProductOverride> overrides) {
        //Reset price
        p.setPrice(null);

        CRC32 checksum = new CRC32();
        checksum.update(i.getAsin().getBytes());
        long ref = checksum.getValue();
        p.ref(ref).slug(String.valueOf(ref))
                .active(true)
                .sku(i.getAsin())
                .url(i.getUrl())
                .image(i.getImage())
                .brand(i.getBrand())
                .weight(PasUtility.calculateWeight(i.getParsedWeight(), getOverride(overrides, OverrideType.WEIGHT)))
                .title(i.getTitle())
                .upc(i.getUpc())
                .gallery(i.gallerizeImages());

        return p;
    }

    public static IProductLang parseProductI18n(IProductLang p, PasItemNode i) {
        p.setFeatures(i.getFeatures());
        p.setTitle(i.getTitle());
        p.setModel(i.getModel());
        p.setLang("en");
        p.setDescription(i.getEditorial());
        p.setBrowseNode(i.getBrowseNode());
        return p;
    }

    public static MerchantStock parseStock(MerchantStock stock, PasItemNode item, List<ProductOverride> overrides) throws PricingException, NoOfferException {
        BigDecimal cost = item.getCost();
        if (cost == null)
            throw new NoOfferException("Offers is null");

        BigDecimal weight = item.getParsedWeight();
        //weight = ;

        if(weight == null)
            throw new NoOfferException("Weight is null");

        if(item.getShippingCharges() == null && !item.isPrime() && !item.isSuperSaver() && !item.isFreeShipping())
            throw new NoOfferException("Non prime/free shipping item");

        double margin = 5, risk = 2, fixed = 1.1;
        double localShipping = 0;

        BigDecimal price = PasUtility.calculatePrice(cost, weight, localShipping, margin, risk, fixed, item.isPrime(), item.isSuperSaver(), item.getShippingCountry());
        price = calculatePrice(price, getOverride(overrides, OverrideType.PRICE));
        int availability =  PasUtility.parseAvailability(item);

        return stock.store("Amazon.com").quantity(BigDecimal.valueOf(99)).cost(cost).availability(availability).allow_backorder(true).price(price).location("USA").discount(item.getSavingsPercentage());
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
            options.add(new VariationOption(dim.getDisplayName(),dim.getName(), dim.getValues().stream().collect(Collectors.toList())));
        }
        p.setVariationOptions(options);
    }

    public static void parseVariationAttributes(Product child, PasItemNode childItem) {
        child.setVariationAttributes(childItem.getVariationAttributes());
    }
}
