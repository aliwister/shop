package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.PurchaseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Purchase} and its DTO {@link PurchaseDTO}.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, MerchantMapper.class, PurchaseItemMapper.class})
public interface PurchaseMapper extends EntityMapper<PurchaseDTO, Purchase> {

    @Mapping(source = "deliveryAddress.id", target = "deliveryAddressId")
    @Mapping(source = "invoiceAddress.id", target = "invoiceAddressId")
    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(source = "merchant", target = "merchantObj")
    PurchaseDTO toDto(Purchase purchase);

    @Mapping(source = "deliveryAddressId", target = "deliveryAddress")
    @Mapping(source = "invoiceAddressId", target = "invoiceAddress")
    @Mapping(source = "merchantId", target = "merchant")
    @Mapping(target = "purchaseItems", ignore = true)
    @Mapping(target = "removePurchaseItem", ignore = true)
    Purchase toEntity(PurchaseDTO purchaseDTO);

    @Named(value = "list")
    @Mapping(source = "deliveryAddress.id", target = "deliveryAddressId")
    @Mapping(source = "invoiceAddress.id", target = "invoiceAddressId")
    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(source = "merchant", target = "merchantObj")
    @Mapping(target="purchaseItems", ignore = true)
    PurchaseDTO toDtoList(Purchase purchase);

    @AfterMapping
    default void afterMapping(@MappingTarget PurchaseDTO target, Purchase source) {
        target.setLength(source.getPurchaseItems().size());
    }

    default Purchase fromId(Long id) {
        if (id == null) {
            return null;
        }
        Purchase purchase = new Purchase();
        purchase.setId(id);
        return purchase;
    }
}
