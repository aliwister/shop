package com.badals.shop.graph.mutation;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.CheckoutCart;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.pojo.Message;
import com.badals.shop.service.pojo.PresignedUrl;
import com.badals.shop.service.*;
import com.badals.shop.service.pojo.AddProductDTO;

import com.badals.shop.service.util.ChecksumUtil;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;


@Component
public class TenantAdminMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(TenantAdminMutation.class);

    private final ProductService productService;
    private final TenantAdminOrderService tenantAdminOrderService;

    private final Pas5Service pasService;

    private final AwsService awsService;

    private final ProductLangService productLangService;

    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;

    @Value("${profileshop.cdnUrl}")
    private String cdnUrl;
    private final ProductIndexService productIndexService;


    public TenantAdminMutation(ProductService productService, TenantAdminOrderService tenantAdminOrderService, Pas5Service pasService, AwsService awsService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService, ProductIndexService productIndexService) {
        this.productService = productService;
        this.tenantAdminOrderService = tenantAdminOrderService;
        this.pasService = pasService;
        this.awsService = awsService;
        this.productLangService = productLangService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.userService = userService;
        this.productIndexService = productIndexService;
    }


    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public Message createMerchantProduct(AddProductDTO dto){
        String t =  TenantContext.getCurrentTenant();
        log.info("Tenant: " + t);
        productService.createMerchantProduct(dto, TenantContext.getCurrentMerchantId(), TenantContext.getCurrentMerchant(), TenantContext.getCurrentTenantId(), TenantContext.getCurrentTenant());
        //return productService.createMerchantProduct(dto, 11L, "Mayaseen", 11L);
        return new Message("New Draft Product created successfully");
    }


    public OrderDTO createPosOrder(CheckoutCart cart, String paymentMethod, String paymentAmount, String ref) {
        return tenantAdminOrderService.createPosOrder(cart, paymentMethod, paymentAmount, ref);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AddProductDTO createProduct(AddProductDTO dto, boolean isSaveES, Long currentMerchantId) {
        return productService.createProduct(dto, isSaveES, currentMerchantId);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AddProductDTO createStub(AddProductDTO dto, boolean isSaveES, Long currentMerchantId) throws Exception {
        return productService.createStub(dto, isSaveES, currentMerchantId);
    }

    public Message importProducts(List<AddProductDTO> products, List<Long> shopIds, String browseNode) {
        String t =  TenantContext.getCurrentTenant();
        productIndexService.importProducts(products, TenantContext.getCurrentMerchantId(), TenantContext.getCurrentMerchant(), TenantContext.getCurrentTenantId(),TenantContext.getCurrentTenant(), shopIds, browseNode);//TenantContext.getCurrentMerchantId(), TenantContext.getCurrentMerchant(), TenantContext.getCurrentTenantId());
        return new Message("success");
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    public PresignedUrl getImageUploadUrl(String filename, String contentType) {
        String t =  TenantContext.getCurrentTenant();
        String m = TenantContext.getCurrentMerchant();
        String fileKey = ChecksumUtil.getChecksum(filename + LocalDate.now());
        String objectKey = "_t/" + m + "/" + fileKey;
        URL url = awsService.presignPutUrl(objectKey, contentType);

        return new PresignedUrl(url.toString(), cdnUrl + "/" + m + "/" + fileKey, "200");
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
        return new PresignedUrl(url.toString(), cdnUrl + "/" + filename, "200");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PresignedUrl getAdminImageUploadUrl(String filename, String merchant, String contentType) {
        String objectKey = "_m/" + merchant + "/" + filename;
        URL url = awsService.presignPutUrl(objectKey, contentType);
        return new PresignedUrl(url.toString(), cdnUrl + "/" + merchant + "/" + filename, "200");
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
        System.out.println(System.getProperty("user.home"));
        String bucketName = "badals-face-bucket";
        String objectKey = "pod/testshop.jpg";
        Region region = Region.EU_CENTRAL_1;
        //S3Client s3Client = S3Client.builder().region(region).credentialsProvider(Credential).build();

        try {
            S3Presigner presigner = S3Presigner.builder().region(region).build();
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

