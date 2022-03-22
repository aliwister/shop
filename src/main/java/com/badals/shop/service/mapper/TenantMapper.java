package com.badals.shop.service.mapper;

import com.badals.shop.domain.Reward;
import com.badals.shop.domain.pojo.PaymentDef;
import com.badals.shop.domain.pojo.PaymentProfile;
import com.badals.shop.domain.pojo.PriceMap;
import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.service.CurrencyService;
import com.badals.shop.service.dto.RewardDTO;
import com.badals.shop.service.dto.TenantDTO;

import org.mapstruct.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Mapper for the entity {@link Tenant} and its DTO {@link TenantDTO}.
 */
@Mapper(componentModel = "spring", uses = {MerchantMapper.class, CustomerMapper.class})
public interface TenantMapper extends EntityMapper<TenantDTO, Tenant> {


/*    @Mapping(target = "removeMerchant", ignore = true)
    @Mapping(target = "removeCustomer", ignore = true)*/
    @Mapping(target = "publicPaymentProfile", source="paymentProfile", qualifiedByName = "unBoxPayments")
    TenantDTO toDto(Tenant reward);

    @Named("unBoxPayments")
    public static List<PaymentDef> unBoxPayments(PaymentProfile paymentProfile) {
        return paymentProfile.getPayments();
    }

    default Tenant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tenant tenant = new Tenant();
        tenant.setId(id);
        return tenant;
    }
}
