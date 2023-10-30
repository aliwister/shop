package com.badals.shop.graph.mutation;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.enumeration.AssetType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.LineItem;
import com.badals.shop.domain.tenant.Checkout;
import com.badals.shop.domain.tenant.S3UploadRequest;
import com.badals.shop.domain.tenant.TenantWishList;
import com.badals.shop.domain.tenant.TenantWishListItem;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.ProfileHashtagDTO;
import com.badals.shop.service.pojo.Message;
import com.badals.shop.service.pojo.PresignedUrl;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.graph.CartResponse;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.badals.shop.service.pojo.CheckoutSession;
import com.badals.shop.service.pojo.PartnerProduct;
import com.badals.shop.service.pojo.ProductEnvelope;
import com.badals.shop.service.TenantCartService;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;


@Component
public class ShopMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(ShopMutation.class);

    private final TenantProductService productService;
    private final TenantSetupService tenantSetupService;
    private final TenantCartService cartService;
    private final TenantWishListService wishlistService;
    private final PricingRequestService pricingRequestService;

    private final CustomerService customerService;

    private final MessageSource messageSource;

    private final UserService userService;

    private final AwsService awsService;

    @Value("${profileshop.cdnUrl}")
    private String cdnUrl;


    public ShopMutation(TenantProductService productService, TenantSetupService tenantSetupService, TenantCartService cartService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService, AwsService awsService, TenantWishListService wishlistService, CustomerService customerService) {
        this.productService = productService;
        this.tenantSetupService = tenantSetupService;
        this.cartService = cartService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.userService = userService;
        this.awsService = awsService;
        this.wishlistService = wishlistService;
        this.customerService = customerService;
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    public CartResponse updateTenantCart(final String secureKey, final List<CartItemDTO> items, boolean isMerge, String coupon) {
        Locale l = LocaleContextHolder.getLocale();
        CartDTO cart = this.cartService.updateCart(secureKey, items, isMerge);
        CartResponse response = new CartResponse();
        response.setCart(cart);
        response.setSuccess(true);
        response.setMessage("Cart Saved Successfully");
        return response;
    }

    public Message addCouponToCart(final String secureKey, String coupon) {
        Locale l = LocaleContextHolder.getLocale();
        return this.cartService.addCouponToCart(secureKey, coupon);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public TenantWishListItem addWishlistItem(Long productId) {
        Customer customer = customerService.getUserWithAuthorities().orElse(null);
        if (customer == null) {
            throw new RuntimeException("User not found");
        }
        Long customerId = customer.getId();
        String tenantId = TenantContext.getCurrentProfile();
        TenantWishList tenantWishList = wishlistService.getCustomerWishListByCustomerAndTenant(tenantId, customerId);
        if (tenantWishList == null) {
            tenantWishList = new TenantWishList();
            tenantWishList.setCustomer(customer);
            tenantWishList.setTenantId(tenantId);
            tenantWishList = wishlistService.createWishList(tenantWishList);
        }
        TenantWishListItem item = new TenantWishListItem();
        item.setWishlist(tenantWishList);
        item.setProductId(productId);
        return wishlistService.addWishlistItem(item);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public TenantWishList removeWishlistItem(Long productId) {
        Customer customer = customerService.getUserWithAuthorities().orElse(null);
        if (customer == null) {
            throw new RuntimeException("User not found");
        }
        Long customerId = customer.getId();
        String tenantId = TenantContext.getCurrentProfile();
        TenantWishList tenantWishList = wishlistService.getCustomerWishListByCustomerAndTenant(tenantId, customerId);
        if (tenantWishList == null) {
            throw new RuntimeException("Wishlist not found");
        }

        wishlistService.removeWishlistItem(tenantId, tenantWishList.getId(), productId);
        tenantWishList.getWishListItems().removeIf(item -> item.getProductId().equals(productId));
        wishlistService.save(tenantWishList);
        return  wishlistService.getCustomerWishListByCustomerAndTenant(tenantId, customerId);
    }

//    public CartResponse convertWishListToCart(final String secureKey){
//        List<CartItemDTO> items;
//
//        return updateTenantCart(secureKey, items, true) ;
//    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    public CheckoutSession createTenantCheckout(final String secureKey, final List<CartItemDTO> items) {
        String token = cartService.createCheckout(secureKey, items);
        return new CheckoutSession("/checkout/" + token + "/address", token);
    }

    public ProductDTO createStubFromSearch(ProductDTO dto, String tag) throws URISyntaxException {
        return productService.createStubFromSearch(dto, tag);
    }

    public ProductDTO removeTag(String ref, String tag) throws URISyntaxException {
        return productService.removeTag(ref, tag);
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    public Checkout createPlusCart(String secureKey, List<LineItem> items) {
        Checkout cart = cartService.createCheckoutPlus(secureKey, items);
        return cart;
    }

    public Checkout createPlusCartAdmin(String secureKey, List<LineItem> items, Long id) {
        Checkout cart = cartService.createCheckoutPlus(secureKey, items, id);
        return cart;
    }
}

