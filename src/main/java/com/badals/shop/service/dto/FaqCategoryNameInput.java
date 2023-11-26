package com.badals.shop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class FaqCategoryNameInput implements Serializable {

    @NotNull
    private Integer position;
    @NotNull
    private String name;
    @NotNull
    private String language;
    private Boolean enabled;
}
