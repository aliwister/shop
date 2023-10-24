//package com.badals.shop.graph;
//
//import com.badals.shop.service.dto.WishListDTO;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//@Data
//@AllArgsConstructor
//public class WishListItemResponse implements Serializable {
//
//    private Long id;
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
//        WishListItemResponse wishListItemResponse = (WishListItemResponse) o;
//        if ("wishListItemResponse.getId()" == null || "getId()" == null) {
//            return false;
//        }
////        return Objects.equals(getId(), cartDTO.getId());
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(1234);
//    }
//
//    @Override
//    public String toString() {
//        return "WishListDTO{";
//    }
//}
