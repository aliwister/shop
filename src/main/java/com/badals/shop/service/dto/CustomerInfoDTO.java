package com.badals.shop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CustomerInfoDTO {
    String firstname;
    String lastname;
    String email;
    List<String> roles;
}
