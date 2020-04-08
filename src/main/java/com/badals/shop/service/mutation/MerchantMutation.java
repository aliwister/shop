package com.badals.shop.service.mutation;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.checkout.helper.PresignedUrl;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.security.DomainUserDetailsService;
import com.badals.shop.service.PricingRequestService;
import com.badals.shop.service.ProductLangService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.ProductLangDTO;
import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.badals.shop.xtra.amazon.PricingException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;


@Component
public class MerchantMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(MerchantMutation.class);

    private final ProductService productService;

    private final Pas5Service pasService;

    private final ProductLangService productLangService;

    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;


    public MerchantMutation(ProductService productService, Pas5Service pasService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService) {
        this.productService = productService;
        this.pasService = pasService;
        this.productLangService = productLangService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.userService = userService;
    }



    public AddProductDTO createMerchantProduct(AddProductDTO dto){
        String t =  TenantContext.getCurrentTenant();
        log.info("Tenant: " + t);
        return productService.createMerchantProduct(dto, TenantContext.getCurrentMerchantId(), TenantContext.getCurrentMerchant(), TenantContext.getCurrentTenantId());
        //return productService.createMerchantProduct(dto, 11L, "Mayaseen", 11L);
    }


    public PresignedUrl getImageUploadUrl(String filename, String contentType) {
        String bucketName = "face-content";
        String t =  TenantContext.getCurrentTenant();
        String m = TenantContext.getCurrentMerchant();
        String objectKey = "_m/" + t + "/" + m + "/" + filename;
        Region region = Region.EU_CENTRAL_1;

        try {
            S3Presigner presigner = S3Presigner.builder().region(region).build();

            PresignedPutObjectRequest presignedRequest =
                    presigner.presignPutObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                            .putObjectRequest(por -> por.bucket(bucketName).key(objectKey).contentType(contentType)));
            // Upload content to the bucket by using this URL
            URL url = presignedRequest.url();
            return new PresignedUrl(url.toString(), "https://cdn.badals.com/"+t + "/" + m + "/" + filename, "200");
        } catch (S3Exception  e) {
            e.getStackTrace();
        }
        return new PresignedUrl("","","");
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

