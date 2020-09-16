package com.badals.shop.service.mutation;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.checkout.helper.PresignedUrl;
import com.badals.shop.service.*;
import com.badals.shop.service.pojo.AddProductDTO;

import com.badals.shop.service.util.S3Util;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.util.List;


@Component
public class MerchantMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(MerchantMutation.class);

    private final ProductService productService;

    private final Pas5Service pasService;

    private final AwsService awsService;

    private final ProductLangService productLangService;

    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;


    public MerchantMutation(ProductService productService, Pas5Service pasService, AwsService awsService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService) {
        this.productService = productService;
        this.pasService = pasService;
        this.awsService = awsService;
        this.productLangService = productLangService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.userService = userService;
    }


    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public Message createMerchantProduct(AddProductDTO dto){
        String t =  TenantContext.getCurrentTenant();
        log.info("Tenant: " + t);
        productService.createMerchantProduct(dto, TenantContext.getCurrentMerchantId(), TenantContext.getCurrentMerchant(), TenantContext.getCurrentTenantId(), TenantContext.getCurrentTenant());
        //return productService.createMerchantProduct(dto, 11L, "Mayaseen", 11L);
        return new Message("New Draft Product created successfully");
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AddProductDTO createProduct(AddProductDTO dto, boolean isSaveES, Long currentMerchantId) {
        return productService.createProduct(dto, isSaveES, currentMerchantId);
    }


    public Message importProducts(List<AddProductDTO> products, List<Long> shopIds, String browseNode) {
        String t =  TenantContext.getCurrentTenant();
        productService.importProducts(products, TenantContext.getCurrentMerchantId(), TenantContext.getCurrentMerchant(), TenantContext.getCurrentTenantId(),TenantContext.getCurrentTenant(), shopIds, browseNode);//TenantContext.getCurrentMerchantId(), TenantContext.getCurrentMerchant(), TenantContext.getCurrentTenantId());
        return new Message("success");
    }

    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public PresignedUrl getImageUploadUrl(String filename, String contentType) {
        String t =  TenantContext.getCurrentTenant();
        String m = TenantContext.getCurrentMerchant();
        String objectKey = "_m/" + m + "/" + filename;
        URL url = awsService.presignPutUrl(objectKey, contentType);
        return new PresignedUrl(url.toString(), "https://cdn.badals.com/"+ m + "/" + filename, "200");
/*        try {
            S3Presigner presigner = S3Presigner.builder().region(region).build();

            PresignedPutObjectRequest presignedRequest =
                    presigner.presignPutObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                            .putObjectRequest(por -> por.bucket(S3Util.getBucketName()).key(objectKey).contentType(contentType)));
            // Upload content to the bucket by using this URL
            URL url = presignedRequest.url();
            return new PresignedUrl(url.toString(), "https://cdn.badals.com/"+t + "/" + m + "/" + filename, "200");
        } catch (S3Exception  e) {
            e.getStackTrace();
        }
        return new PresignedUrl("","","");*
        return adminMutation.getUploadUrl(objectKey, contentType);*/
    }
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public PresignedUrl getAdminFile(String filename, String contentType) {
        String objectKey = filename;
        URL url = awsService.presignGetUrl(objectKey, contentType);
        return new PresignedUrl(url.toString(), "https://cdn.badals.com/" + filename, "200");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PresignedUrl getAdminImageUploadUrl(String filename, String merchant, String contentType) {
        String objectKey = "_m/" + merchant + "/" + filename;
        URL url = awsService.presignPutUrl(objectKey, contentType);
        return new PresignedUrl(url.toString(), "https://cdn.badals.com/"+ merchant + "/" + filename, "200");
/*        try {
            S3Presigner presigner = S3Presigner.builder().region(region).build();

            PresignedPutObjectRequest presignedRequest =
                    presigner.presignPutObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                            .putObjectRequest(por -> por.bucket(S3Util.getBucketName()).key(objectKey).contentType(contentType)));
            // Upload content to the bucket by using this URL
            URL url = presignedRequest.url();
            return new PresignedUrl(url.toString(), "https://cdn.badals.com/"+t + "/" + m + "/" + filename, "200");
        } catch (S3Exception  e) {
            e.getStackTrace();
        }
        return new PresignedUrl("","","");*
        return adminMutation.getUploadUrl(objectKey, contentType);*/
    }

    public static void main(String args[]) {

        String bucketName = "badals-face-bucket";
        String objectKey = "pod/testshop.jpg";
        Region region = Region.EU_CENTRAL_1;
        //S3Client s3Client = S3Client.builder().region(region).credentialsProvider(Credential).build();

        try {
            S3Presigner presigner = S3Presigner.builder().credentialsProvider(ProfileCredentialsProvider.create()).region(region).build();
            PresignedPutObjectRequest presignedRequest =
                    presigner.presignPutObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                            .putObjectRequest(por -> por.bucket(bucketName).key(objectKey)));
            // Upload content to the bucket by using this URL
            URL url = presignedRequest.url();
            System.out.println(url.toString());
        } catch (S3Exception  e) {
            e.getStackTrace();
        }
    }
}

