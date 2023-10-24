//package com.badals.shop.service.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//@Data
//@AllArgsConstructor
//public class WishListItemDTO implements Serializable {
//
//    private Long id;
//    private int quantity;
//    private Long productId;
//    private String tenantId;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//
//        WishListItemDTO wishlistItemDTO = (WishListItemDTO) o;
//        if (wishlistItemDTO.getId() == null || getId() == null) {
//            return false;
//        }
//        return Objects.equals(getId(), wishlistItemDTO.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(1234);
//    }
//
//    @Override
//    public String toString() {
//        return "WishListItemDTO{" +
//            "id=" + getId() +
//            ", quantity=" + getQuantity() +
//            ", productId=" + getProductId() +
//            ", tenantId='" + getTenantId() + "'" +
//            "}";
//    }
//}
