package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.service.dto.PurchaseItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseItem} and its DTO {@link PurchaseItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {PurchaseMapper.class, OrderItemMapper.class})
public interface PurchaseItemMapper extends EntityMapper<PurchaseItemDTO, PurchaseItem> {

    @Mapping(source = "purchase.id", target = "purchaseId")
    PurchaseItemDTO toDto(PurchaseItem purchaseItem);

    @Mapping(source = "purchaseId", target = "purchase")
/*    @Mapping(target = "orderItems", ignore = true)*/
    PurchaseItem toEntity(PurchaseItemDTO purchaseItemDTO);

    default PurchaseItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setId(id);
        return purchaseItem;
    }
}
