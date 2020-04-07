package com.badals.shop.domain.checkout.helper;

import lombok.Data;

@Data
public class PresignedUrl {
    String uploadUrl;
    String imageUrl;

    String status;

    public PresignedUrl(String uploadUrl, String imageUrl, String status) {
        this.uploadUrl = uploadUrl;
        this.imageUrl = imageUrl;
        this.status = status;
    }
}
