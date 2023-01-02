package com.badals.shop.service.mapper;

import com.badals.shop.domain.Reward;
import com.badals.shop.domain.RewardLang;
import com.badals.shop.domain.tenant.TenantStock;
import com.badals.shop.service.dto.RewardDTO;
import com.badals.shop.service.dto.StockDTO;
import com.badals.shop.service.pojo.PartnerStock;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Mapper for the entity {@link Reward} and its DTO {@link StockDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TenantStockMapper extends EntityMapper<PartnerStock, TenantStock> {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.ref", target = "productRef")
    PartnerStock toDto(TenantStock source);

    //@Mapping(source = "productId", target = "product")
    TenantStock toEntity(PartnerStock target);

    @AfterMapping
    default void afterMapping(@MappingTarget PartnerStock target, TenantStock source) {

    }

    default TenantStock fromId(Long id) {
        if (id == null) {
            return null;
        }
        TenantStock stock = new TenantStock();
        stock.setId(id);
        return stock;
    }
}
