package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.service.dto.CartRuleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CartRule} and its DTO {@link CartRuleDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, TenantHashtagMapper.class})
public interface CartRuleMapper extends EntityMapper<CartRuleDTO, CartRule> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "hashtagRestriction.id", target = "hashtagRestrictionId")
    @Mapping(source = "hashtagRestriction.name", target = "hashtagRestrictionEn")
    CartRuleDTO toDto(CartRule cartRule);

    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "hashtagRestrictionId", target = "hashtagRestriction")
    CartRule toEntity(CartRuleDTO cartRuleDTO);

    default CartRule fromId(Long id) {
        if (id == null) {
            return null;
        }
        CartRule cartRule = new CartRule();
        cartRule.setId(id);
        return cartRule;
    }
}
