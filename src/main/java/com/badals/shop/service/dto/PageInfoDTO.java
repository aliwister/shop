package com.badals.shop.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class PageInfoDTO implements Serializable {
    @NotNull
    private String slug;
    @NotNull
    private String language;
    @NotNull
    private String info;
}
