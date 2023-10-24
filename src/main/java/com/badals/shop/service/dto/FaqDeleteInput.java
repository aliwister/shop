package com.badals.shop.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class FaqDeleteInput {
    @NotNull
    private Long id;
    private String language;
}
