package com.badals.shop.graph.mutation;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.enumeration.AssetType;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.tenant.*;
import com.badals.shop.service.dto.*;
import com.badals.shop.service.pojo.*;
import com.badals.shop.service.*;

import com.badals.shop.service.util.ChecksumUtil;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
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
import java.util.Objects;


@Component
public class PartnerMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(PartnerMutation.class);

    private final TenantAdminProductService productService;
    private final TenantAdminOrderService orderService;
    private final AwsService awsService;
    private final MessageSource messageSource;

    private final UserService userService;
    private final TenantSetupService setupService;
    private final PageInfoService pageInfoService;
    private final FaqCategoryService faqCategoryService;
    private final FaqQAService faqQAService;
    private final TenantCartRuleService tenantCartRuleService;
    private final TenantStockService tenantStockService;


    @Value("${profileshop.cdnUrl}")
    private String cdnUrl;
    public PartnerMutation(TenantAdminProductService productService, TenantAdminOrderService orderService, AwsService awsService, MessageSource messageSource, UserService userService, TenantSetupService setupService, PageInfoService pageInfoService, FaqCategoryService faqCategoryService, FaqQAService faqQAService, TenantCartRuleService tenantCartRuleService, TenantStockService tenantStockService) {
        this.productService = productService;
        this.orderService = orderService;
        this.awsService = awsService;
        this.messageSource = messageSource;
        this.userService = userService;
        this.setupService = setupService;
        this.pageInfoService = pageInfoService;
        this.faqCategoryService = faqCategoryService;
        this.faqQAService = faqQAService;
        this.tenantCartRuleService = tenantCartRuleService;
        this.tenantStockService = tenantStockService;
    }
/*

*/


    public OrderDTO createPosOrder(Checkout cart, String paymentMethod, String paymentAmount, String ref) {
        return orderService.createPosOrder(cart, paymentMethod, paymentAmount, ref);

    }
    public Message voidOrder(Long id) {
        return orderService.voidOrder(id);

    }
/*    public Message importProducts(List<AddProductDTO> products, List<Long> shopIds, String browseNode) {
        String t =  TenantContext.getCurrentTenant();
        productIndexService.importProducts(products, TenantContext.getCurrentMerchantId(), TenantContext.getCurrentMerchant(), TenantContext.getCurrentTenantId(),TenantContext.getCurrentTenant(), shopIds, browseNode);//TenantContext.getCurrentMerchantId(), TenantContext.getCurrentMerchant(), TenantContext.getCurrentTenantId());
        return new Message("success");
    }*/

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT')")
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT')")
    public ProductEnvelope savePartnerProduct(PartnerProduct product) throws ProductNotFoundException {
        PartnerProduct p = null;
        StringBuilder message = new StringBuilder();
        Integer code = 202;

        try {
            p = productService.savePartnerProduct(product, false);
            message.append("Success");
        }
        catch(Throwable e) {
            e.printStackTrace();
            while (e != null) {
                message.append(e.getMessage());
                e = e.getCause();
            }

            code = 400;
        }
        log.error(message.toString());
        return new ProductEnvelope(p, message.toString(), code);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT')")
    public ProductEnvelope savePartnerProductPrice(PartnerProduct product) throws ProductNotFoundException {
        PartnerProduct p = null;
        StringBuilder message = new StringBuilder();
        Integer code = 202;

        try {
            p = productService.savePartnerProductPrice(product, false);
            message.append("Success");
        }
        catch(Throwable e) {
            e.printStackTrace();
            while (e != null) {
                message.append(e.getMessage());
                e = e.getCause();
            }

            code = 400;
        }
        log.error(message.toString());
        return new ProductEnvelope(p, message.toString(), code);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT')")
    public PartnerStock updateStock(PartnerStock stock) throws ProductNotFoundException {
        return tenantStockService.update(stock);
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT')")
    public PresignedUrl getPartnerImageUploadUrl(String filename, String contentType, AssetType assetType) {
        return productService.getS3UploadUrl(filename, contentType, assetType);
    }


    public Message completeUpload(Long fileHandle) {
        S3UploadRequest request = productService.findUploadRequest(fileHandle);
        if (request.getAssetType() == AssetType.LOGO)
            setupService.updateLogo(request);

        setupService.addMedia(request);

        return new Message("success", "200");
    }

    public ProfileHashtagDTO saveTenantTag (ProfileHashtagDTO hashtag) throws NoSuchFieldException {
        return setupService.saveTag(hashtag);
    }
    public Message deleteTenantTag (Long id) {
        setupService.deleteTag(id);
        return new Message("Success");
    }

    public Message setSliderList(String locale, List<String> images) {
        setupService.setSliders(locale, images);
        return new Message("success");
    }

    public Message setSocialProfile(String locale, List<Attribute> profiles) {
        setupService.setSocialProfile(locale, profiles);
        return new Message("success");
    }



    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT')")
    public Message publishProduct(Long id) throws ProductNotFoundException {
        productService.setProductPublished(id, true);
        return new Message("Product published successfully");
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT')")
    public Message unpublishProduct(Long id) throws ProductNotFoundException {
        productService.setProductPublished(id, false);
        return new Message("Product set to draft successfully");
    }
    public Message setOrderState(OrderState value) {
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT')")
    public Message deleteProduct(Long id) throws ProductNotFoundException {
        productService.deleteProduct(id);
        return new Message("ok");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public Page createPageInfo(PageInfoInput info){
        return pageInfoService.createPageInfo(info);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public Message removePageInfo(Long id) {
        PageInfo pageInfo = pageInfoService.getPageInfoById(id);
        if (pageInfo == null)
            return new Message("Page info not found", "404");
        pageInfoService.deleteByIdAndAndTenantId(pageInfo, id, TenantContext.getCurrentProfile());

        return new Message("ok");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public Message removePage(String slug){
        Page page = pageInfoService.getPageBySlug(slug);
        if (page == null)
            return new Message("Page not found", "404");
        pageInfoService.deletePage(page.getId());
        return new Message("ok");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public PageInfo updatePageInfo(PageInfoInput info) throws Exception {
        return pageInfoService.updatePageInfo(info);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public Page updatePage(PageInput pageInput) throws Exception {
        return pageInfoService.updatePage(pageInput);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public TenantFaqCategory createFaqCategoryName(FaqCategoryNameInput faqCategoryNameInput){
        return faqCategoryService.addCategory(faqCategoryNameInput);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public TenantFaqQA createFaqQA(FaqQAInput faqQAInput){
        return faqQAService.addQA(faqQAInput);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public TenantFaqCategory updateFaqCategoryName(FaqCategoryNameInput faqCategoryNameInput){
        return faqCategoryService.updateCategory(faqCategoryNameInput);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public TenantFaqQA updateFaqQA(FaqQAInput faqQAInput){
        return faqQAService.updateQA(faqQAInput);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public Message removeFaqCategoryName(FaqDeleteInput faqDeleteInput) {
        return faqCategoryService.deleteCategory(faqDeleteInput);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public Message removeFaqQA(FaqDeleteInput faqDeleteInput){
        return faqQAService.deleteQA(faqDeleteInput);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public TenantCartRuleDTO createCoupon(TenantCartRuleDTO input){
        return tenantCartRuleService.addCartRule(input);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT','ROLE_USER')")
    public TenantCartRuleDTO updateCoupon(String coupon, TenantCartRuleDTO input){
        return tenantCartRuleService.updateCartRule(coupon, input);
    }

    public Message deleteCoupon(String coupon){
        tenantCartRuleService.deleteCartRule(coupon);
        return new Message("ok");
    }
}

