//package com.badals.shop.service.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.util.List;
//import java.util.Objects;
//
//@Data
//@AllArgsConstructor
//public class WishListDTO implements Serializable {
//
//    private Long id;
//    private Long customerId;
//    private Long currencyId;
//    private String tenantId;
//    private String adjustments;
//    private List<WishListItemDTO> items;
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
//        WishListDTO wishlistDTO = (WishListDTO) o;
//        if (wishlistDTO.getId() == null || getId() == null) {
//            return false;
//        }
//        return Objects.equals(getId(), wishlistDTO.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(1234);
//    }
//
//    @Override
//    public String toString() {
//        return "WishListDTO{" +
//            "id=" + getId() +
//            ", customerId=" + getCustomerId() +
//            ", currencyId=" + getCurrencyId() +
//            ", tenantId='" + getTenantId() + "'" +
//            ", adjustments='" + getAdjustments() + "'" +
//            "}";
//    }
//}
