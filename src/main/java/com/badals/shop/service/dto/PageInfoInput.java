package com.badals.shop.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class PageInfoInput implements Serializable {
    @NotNull
    private String slug;
    @NotNull
    private String language;
    @NotNull
    private String info;
    private Boolean isImportant;
    private Boolean enabled;
}
