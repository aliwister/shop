package com.badals.shop.service.mapper;

import com.badals.shop.domain.pojo.*;
import com.badals.shop.domain.tenant.Tenant;

import com.badals.shop.service.dto.TenantDTO;

import org.mapstruct.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.*;
import java.util.stream.Collectors;
import com.badals.shop.service.pojo.Partner;
/**
 * Mapper for the entity {@link Tenant} and its DTO {@link TenantDTO}.
 */
@Mapper(componentModel = "spring", uses = {MerchantMapper.class, CustomerMapper.class})
public interface TenantMapper extends EntityMapper<TenantDTO, Tenant> {


/*    @Mapping(target = "removeMerchant", ignore = true)
    @Mapping(target = "removeCustomer", ignore = true)*/
    @Mapping(target = "publicPaymentProfile", source="paymentProfile", qualifiedByName = "unBoxPayments")
    //@Mapping(target = "socialList", source="socialProfile", qualifiedByName = "unBoxSocial")
    TenantDTO toDto(Tenant tenant);

    @Mapping(target = "publicPaymentProfile", source="paymentProfile", qualifiedByName = "unBoxPayments")
    //@Mapping(target = "socialList", source="socialProfile", qualifiedByName = "unBoxSocial")
    Partner toPartnerDto(Tenant tenant);

    @Named("unBoxPayments")
    public static List<PaymentDef> unBoxPayments(PaymentProfile paymentProfile) {
        if (paymentProfile == null) return null;
        return paymentProfile.getPayments();
    }
    @AfterMapping
    default void afterMapping(@MappingTarget TenantDTO target, Tenant source, @Context Locale locale) {
        SocialProfile socialProfile = source.getSocialProfile();
        if (socialProfile != null) {
            final Map<?, String> profiles;
            if (socialProfile.getMap().get(locale.toString()) != null)
                profiles = socialProfile.getMap().get(locale.toString());
            else if("xxx" != null)
                profiles = socialProfile.getMap().get("xx");
            else
                profiles = null;
            if(profiles != null )
                target.setSocialList(profiles.keySet().stream().map(x -> new Attribute(x.toString(), profiles.get(x))).collect(Collectors.toList()));
        };
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
