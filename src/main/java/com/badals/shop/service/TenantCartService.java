package com.badals.shop.service;

import com.badals.shop.domain.CheckoutCart;
import com.badals.shop.domain.Customer;
import com.badals.shop.service.mapper.CheckoutAddressMapper;
import com.badals.shop.service.mapper.CheckoutLineItemMapper;
import com.badals.shop.domain.enumeration.CartState;
import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.domain.tenant.TenantCartItem;
import com.badals.shop.domain.tenant.TenantCheckout;
import com.badals.shop.repository.*;
import com.badals.shop.repository.projection.CartItemInfo;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.badals.shop.service.mapper.TenantCartMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TenantCart}.
 */
@Service
@Transactional
public class TenantCartService {

    private final Logger log = LoggerFactory.getLogger(TenantCartService.class);

    private final TenantCartRepository cartRepository;
    private final TenantCheckoutRepository checkoutRepository;
    private final TenantProductRepository productRepository;
    private final TenantCustomerRepository customerRepository;
    private final TenantCartItemRepository cartItemRepository;

    private final TenantCartMapper cartMapper;
    private final UserService userService;
    private final CustomerService customerService;
    private final TenantProductService productService;

    private final CheckoutLineItemMapper checkoutLineItemMapper;
    private final CheckoutAddressMapper checkoutAddressMapper;

    public TenantCartService(TenantCartRepository cartRepository, TenantCheckoutRepository checkoutRepository, TenantProductRepository productRepository, TenantCustomerRepository customerRepository, TenantCartItemRepository cartItemRepository, TenantCartMapper cartMapper, UserService userService, CustomerService customerService, TenantProductService productService, CheckoutLineItemMapper checkoutLineItemMapper, CheckoutAddressMapper checkoutAddressMapper) {
        this.cartRepository = cartRepository;
        this.checkoutRepository = checkoutRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
        this.userService = userService;
        this.customerService = customerService;
        this.productService = productService;
        this.checkoutLineItemMapper = checkoutLineItemMapper;
        this.checkoutAddressMapper = checkoutAddressMapper;
    }


    public static String createUIUD() {
        // Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Save a cart.
     *
     * @param cartDTO the entity to save.
     * @return the persisted entity.
     */
    public CartDTO save(CartDTO cartDTO) {
        log.debug("Request to save ProfileCart : {}", cartDTO);
        TenantCart cart = cartMapper.toEntity(cartDTO);
        cart = cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }


    /**
     * Save a cart.
     *
     * @param cartDTO the entity to save.
     * @return the persisted entity.
     */
    public CartDTO save(CartDTO cartDTO, List<CartItemDTO> items) {
        log.debug("Request to save ProfileCart : {}", cartDTO);
        TenantCart cart = cartMapper.toEntity(cartDTO);
        //cart = cartRepository.save(cart);
        return save(cart, items); //cartMapper.toDto(cart);
    }

    /**
     * Get all the carts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CartDTO> findAll() {
        log.debug("Request to get all Carts");
        return cartRepository.findAll().stream()
            .map(cartMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one cart by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CartDTO> findOne(Long id) {
        log.debug("Request to get ProfileCart : {}", id);
        return cartRepository.findById(id)
            .map(cartMapper::toDto);
    }

    /**
     * Delete the cart by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProfileCart : {}", id);
        cartRepository.deleteById(id);
    }

    @Transactional
    public CartDTO updateCart(String secureKey, List<CartItemDTO> items, boolean isMerge) {
        TenantCart cart = null;
        Customer loginUser = customerService.getUserWithAuthorities().orElse(null);
        log.info("Logged in user " + loginUser);
        // secureKey always null
        if(secureKey != null) {
            cart = cartRepository.findBySecureKey(secureKey).orElse(null);
        }

        // Never get called if user not logged in
        if(loginUser == null) {
            if (cart == null || cart.getCartState() != CartState.UNCLAIMED) {
                cart = new TenantCart();
                cart.setSecureKey(createUIUD());
            }
            return this.mergeCart(cart, items, isMerge);
        }

        //logged in user - always
        if (cart == null || cart.getCartState() == CartState.UNCLAIMED) {
            cart = this.getCartByCustomer(loginUser);
            if (isMerge)
                return this.mergeCart(cart, items, false);
            else
                return this.mergeCart(cart, items, false);
        }

        if (cart.getCartState() == CartState.CLAIMED) {
            if( loginUser.getId().longValue() == cart.getCustomerId() )
                return this.mergeCart(cart, items, isMerge);

            cart = this.getCartByCustomer(loginUser);
            if (isMerge)
                return this.mergeCart(cart, items, isMerge);
            else
                return this.save(cart, items);
        }
        //if (cart.getCartState() == CartState.CLOSED) {
        cart = this.getCartByCustomer(loginUser);
        return this.mergeCart(cart, items, isMerge);
        //}
    }

    private CartDTO mergeCart(TenantCart cart, List<CartItemDTO> items, Boolean isMerge) {
        List<TenantCartItem> cartItems = cart.getCartItems();
        //isMerge = false;

        if(items != null) {
            for (CartItemDTO dto : items) {
                TenantCartItem existing = cartItems != null?cartItems.stream().filter(x -> x.getProductId().equals(dto.getProductId())).findAny().orElse(null):null;
                if (existing != null) {
                    if(isMerge)
                        existing.setQuantity(existing.getQuantity() + dto.getQuantity());
                    else
                        existing.setQuantity(dto.getQuantity());

                    if(existing.getQuantity() < 1) {
                        cartItems.remove(existing);
                    }
                }
                else {
                    // Check product exists
                    //ProfileProduct p = productRepository.findOneByRef(dto.getProductId().toString()).orElse(null);
                    if (dto.getQuantity() > 0 && productService.exists(dto.getProductId().toString()) ) {
                        TenantCartItem newCartItem = new TenantCartItem();
                        newCartItem.setProductId(dto.getProductId());
                        newCartItem.setQuantity(dto.getQuantity());
                        cart.addCartItem(newCartItem);

                    }
                }
            }
            //cart.setTenantId(TenantContext.getCurrentProfile());
            //cart.setCartItems(cartItems);
            Customer loginUser = customerService.getUserWithAuthorities().orElse(null);
            if(loginUser != null)
                cart.setCustomer(loginUser);
            cart = cartRepository.saveAndFlush(cart);
            cartRepository.refresh(cart);
        }
        return cartMapper.toDto(cart);
    }


    /*
             for (CartItem c : content2) {
            CartItem cc = content1.stream().filter(x -> x.getId() == c.getId()).findAny().orElse(null);
            if(cc != null)
            	content1.get(index).setQuantity(cc.getQuantity() + c.getQuantity());
            else
            	content1.add(cc);
            index += 1;
         }

     */

    private CartDTO save(TenantCart cart, List<CartItemDTO> items) {
        this.setItems(cart, items);
        cart = cartRepository.saveAndFlush(cart);
        cartRepository.refresh(cart);
        return cartMapper.toDto(cart);
    }

    private TenantCart getCartByCustomer(Customer loginUser) {
        List<TenantCart> carts = cartRepository.findByCustomerIdAndCartStateOrderByIdDesc(loginUser.getId(), CartState.CLAIMED);
        if (carts.size() == 0) {
            TenantCart cart = new TenantCart();
            //cart.setCustomer(loginUser);
            cart.setSecureKey(createUIUD());
            cart.setCartState(CartState.CLAIMED);
            return cart;
        }
        return carts.get(0);
    }

    private void setItems(TenantCart cart, List<CartItemDTO> items) {
        //List<CartItem> cartItems = new ArrayList<>();
        cart.getCartItems().clear();
        if(items != null)
            for(CartItemDTO dto : items) {
                Long ref = dto.getProductId();
    /*            if(ref != null && productService.exists(ref)) {
                    CartItem item = cartItemMapper.toEntity(dto);
                    cart.addCartItem(item);
                }*/
            }
        //cart.setCartItems(cartItems);
    }

    @Transactional
    public String createCheckout(String secureKey, List<CartItemDTO> items) {
        return this.createCheckoutWithCart(secureKey, items).getSecureKey();
    }

    @Transactional
    public TenantCheckout createCheckoutWithCart(String secureKey, List<CartItemDTO> items) {
        //ProfileCart cart = cartRepository.findBySecureKey(secureKey).orElse(new ProfileCart()); //cartMapper.toEntity(cartDTO);
        Locale locale = LocaleContextHolder.getLocale();
        String currency = Currency.getInstance(locale).getCurrencyCode();

        Customer customer = customerService.getUserWithAuthorities().orElse(null);
        TenantCart cart = null;

        if (customer != null) {
            //customer = customerRepository.findByIdJoinAddresses(cart.getCustomer().getId()).orElse(null);
            cart = this.getCartByCustomer(customer);
            //cart = cartRepository.getCartByCustomerJoinAddresses(cart.getId());
            //cart = cartRepository.getCartByCustomerJoinAddresses(customer.getId());
        }
        else {
            cart = cartRepository.findBySecureKey(secureKey).get();
        }
        String currencyPath = "$.prices."+currency;
        List<CartItemInfo> cartItems = cartItemRepository.findCartItemsWithProductNative(cart.getId(), currencyPath);
        TenantCheckout checkout = checkoutRepository.findBySecureKeyAndCheckedOut(cart.getSecureKey(),false).orElse(new TenantCheckout());
        checkout.setSecureKey(cart.getSecureKey());
        checkout.setCheckedOut(false);
        checkout.setItems(cartItems.stream().map(checkoutLineItemMapper::cartItemToLineItem).collect(Collectors.toList()));
        checkout.setCartWeight(cartItems.stream().map(x -> x.getWeight()).reduce(BigDecimal.ZERO, BigDecimal::add));

        checkout.setCurrency(currency);

        checkout.setLock(false);
        if (customer != null && customer.getAddresses() != null && customer.getAddresses().size() > 0)
            checkout.setAddresses(customer.getAddresses().stream().map(checkoutAddressMapper::addressToAddressPojo).filter(x->x.getPlusCode() != null).collect(Collectors.toList()));

        if (customer != null) {
            checkout.setName(cart.getCustomer().getFirstname() + " " + cart.getCustomer().getFirstname());
            checkout.setEmail(cart.getCustomer().getEmail());
        }
        else
            checkout.setGuest(true);

        checkout = checkoutRepository.save(checkout);
        return checkout;
    }

    public void closeCart(String secureKey) {
        TenantCart cart = cartRepository.findBySecureKey(secureKey).orElse(null);
        if(cart == null)
            return;
        cart.setCartState(CartState.CLOSED);
        cartRepository.save(cart);
    }

   public CheckoutCart createCustomCart(CheckoutCart cart) {
        return cart;
   }
}
