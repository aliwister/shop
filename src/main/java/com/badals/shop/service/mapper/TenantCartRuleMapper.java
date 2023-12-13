package com.badals.shop.service.mapper;

import com.badals.shop.domain.CartRule;
import com.badals.shop.domain.pojo.PriceMap;
import com.badals.shop.domain.tenant.TenantCartRule;
import com.badals.shop.service.dto.CartRuleDTO;
import com.badals.shop.service.dto.TenantCartRuleDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

/**
 * Mapper for the entity {@link CartRule} and its DTO {@link CartRuleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TenantCartRuleMapper extends EntityMapper<TenantCartRuleDTO, TenantCartRule> {

    ObjectWriter ow = new ObjectMapper().writer();
    ObjectMapper om = new ObjectMapper();

    TenantCartRuleDTO toDto(TenantCartRule cartRule);

    TenantCartRule toEntity(TenantCartRuleDTO cartRuleDTO);

    default TenantCartRule fromId(Long id) {
        if (id == null) {
            return null;
        }
        TenantCartRule cartRule = new TenantCartRule();
        cartRule.setId(id);
        return cartRule;
    }

    default String priceMapToString(PriceMap priceMap) {
        try {
            return ow.writeValueAsString(priceMap);
        } catch (JsonProcessingException e) {
            // Handle the exception according to your needs
            e.printStackTrace();
            return null;
        }
    }

    default PriceMap stringToPriceMap(String priceMapString) {
        try {
            return om.readValue(priceMapString, PriceMap.class);
        } catch (IOException e) {
            // Handle the exception according to your needs
            e.printStackTrace();
            return null;
        }
    }
}
