package com.badals.shop.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GoogleLoginVM {

    @NotNull
    private String idToken;

        @Override
        public String toString() {
            return "GoogleLoginVM{" +
                "idToken='" + idToken + '\'' +
                '}';
        }
}
