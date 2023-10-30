//package com.badals.shop.service.mapper;
//
//import com.badals.shop.domain.tenant.TenantWishList;
//import com.badals.shop.service.dto.WishListDTO;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring", uses = {TenantWishListItemMapper.class})
//public interface TenantWishListMapper extends EntityMapper<WishListDTO, TenantWishList>{
//
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "customer.id", target = "customerId")
//    @Mapping(source = "tenantId", target = "tenantId")
//    @Mapping(source = "wishlistItems", target = "items")
//    WishListDTO toDto(TenantWishList wishList);
//}
