package com.badals.shop.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PageInput {
    @NotNull
    private Long id;
    private Boolean isImportant;
}
