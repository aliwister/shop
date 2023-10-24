package com.badals.shop.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class FaqQAInput implements Serializable {

        @NotNull
        private Long categoryId;
        @NotNull
        private Integer position;
        @NotNull
        private String question;
        @NotNull
        private String answer;
        @NotNull
        private String language;

}
