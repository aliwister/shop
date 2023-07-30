package com.badals.shop.service.mapper;

import com.badals.shop.domain.enumeration.DiscountReductionType;
import com.badals.shop.domain.pojo.AdjustmentProfile;
import com.badals.shop.domain.pojo.OrderAdjustment;
import com.badals.shop.domain.pojo.PriceMap;
import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.domain.tenant.TenantCartItem;
import com.badals.shop.service.CurrencyService;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import org.mapstruct.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Currency;
import java.util.List;
import java.util.Locale;


@Mapper(componentModel = "spring", uses = {TenantCartItemMapper.class})
public interface TenantCartMapper extends EntityMapper<CartDTO, TenantCart> {
    //@Mapping(target = "adjustments", source="cartAdjustments", qualifiedByName = "withCurrencyConversionMap")
    CartDTO toDto(TenantCart cart);

    TenantCart toEntity(CartDTO cartDTO);

    default TenantCart fromId(Long id) {
        if (id == null) {
            return null;
        }
        TenantCart cart = new TenantCart();
        cart.setId(id);
        return cart;
    }

    @AfterMapping
    default void afterMapping(@MappingTarget CartDTO target, TenantCart source) {
        Locale locale = LocaleContextHolder.getLocale();
        target.setCurrency(Currency.getInstance(locale).getCurrencyCode());
    }

    @Named("withCurrencyConversionMap")
    public static List<OrderAdjustment> withCurrencyConversionMap(List<AdjustmentProfile> cartAdjustments) {
        Locale locale = LocaleContextHolder.getLocale();
        String targetCurrency = Currency.getInstance(locale).getCurrencyCode();

        for (AdjustmentProfile profile : cartAdjustments) {
            PriceMap priceMap = profile.getDiscount();
            String price = priceMap.getPriceForCurrency(targetCurrency);
            if(price == null) {
                String baseCurrency = priceMap.getBase();
                price = priceMap.getPriceForCurrency(baseCurrency);
                if (profile.getType().equals(DiscountReductionType.AMOUNT))
                    price = CurrencyService.convert(price, baseCurrency, targetCurrency);

                //else
            }
        }

/*        String price = priceMap.getPriceForCurrency(targetCurrency);
        if(price == null) {
            String baseCurrency = priceMap.getBase();
            price = priceMap.getPriceForCurrency(baseCurrency);
            price = CurrencyService.convert(price, baseCurrency, targetCurrency);
        }
        return price;*/
        return null;
    }
}

