package com.badals.shop.graph.mutation;

import com.badals.shop.domain.checkout.helper.PresignedUrl;
import com.badals.shop.service.*;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class AdminMutation implements GraphQLMutationResolver {

    private final AwsService awsService;

    public AdminMutation(AwsService awsService) {
        this.awsService = awsService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PresignedUrl getUploadUrl(String filename, String contentType) {
        String objectKey = filename;
        URL url = awsService.presignPutUrl(objectKey, contentType);
        return new PresignedUrl(url.toString(), "https://cdn.badals.com/" + filename, "200");
    }

}

