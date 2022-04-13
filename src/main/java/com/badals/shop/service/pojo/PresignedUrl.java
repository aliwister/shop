package com.badals.shop.service.pojo;

import lombok.Data;

@Data
public class PresignedUrl {
    String uploadUrl;
    String imageUrl;
    String saveUrl;
    Long fileHandle;
    String status;

    public PresignedUrl(String uploadUrl, String imageUrl, String status) {
        this.uploadUrl = uploadUrl;
        this.imageUrl = imageUrl;
        this.status = status;
    }


    public PresignedUrl(Long fileHandle, String uploadUrl, String imageUrl, String saveUrl, String status) {
        this.fileHandle = fileHandle;
        this.uploadUrl = uploadUrl;
        this.imageUrl = imageUrl;
        this.saveUrl = saveUrl;
        this.status = status;

    }
}
