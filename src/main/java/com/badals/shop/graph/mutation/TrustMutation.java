package com.badals.shop.graph.mutation;

import com.badals.shop.service.pojo.PresignedUrl;
import com.badals.shop.service.*;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class TrustMutation implements GraphQLMutationResolver {

    private final LegacyAwsService awsService;

    public TrustMutation(LegacyAwsService awsService) {
        this.awsService = awsService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PresignedUrl getUploadUrl(String filename, String contentType) {
        String objectKey = filename;
        URL url = awsService.presignPutUrl(objectKey, contentType);
        return new PresignedUrl(url.toString(), "https://cdn.badals.com/" + filename, "200");
    }

}

