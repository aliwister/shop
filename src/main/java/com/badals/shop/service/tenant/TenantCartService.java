package com.badals.shop.service.tenant;

import com.badals.shop.domain.*;
import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.domain.enumeration.CartState;
import com.badals.shop.repository.*;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.badals.shop.service.mapper.ProfileCartMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ProfileCart}.
 */
@Service
@Transactional
public class TenantCartService {

    private final Logger log = LoggerFactory.getLogger(TenantCartService.class);

    private final ProfileCartRepository cartRepository;
    private final ProfileProductRepository productRepository;

    private final ProfileCartMapper cartMapper;
    private final UserService userService;
    private final TenantProductService productService;

    public TenantCartService(ProfileCartRepository cartRepository, ProfileProductRepository productRepository, ProfileCartMapper cartMapper, UserService userService, TenantProductService productService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
        this.userService = userService;
        this.productService = productService;
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
        ProfileCart cart = cartMapper.toEntity(cartDTO);
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
        ProfileCart cart = cartMapper.toEntity(cartDTO);
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

    public CartDTO updateCart(String secureKey, List<CartItemDTO> items, boolean isMerge) {
        ProfileCart cart = null;
        Customer loginUser = userService.getUserWithAuthorities().orElse(null);
        log.info("Logged in user " + loginUser);
        // secureKey always null
        if(secureKey != null) {
            cart = cartRepository.findBySecureKey(secureKey).orElse(null);
        }

        // Never get called if user not logged in
        if(loginUser == null) {
            if (cart == null || cart.getCartState() != CartState.UNCLAIMED) {
                cart = new ProfileCart();
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

        //Not reachable
        if (cart.getCartState() == CartState.CLAIMED) {
            if( loginUser.getId() == cart.getCustomer().getId() )
                return this.mergeCart(cart, items, false);

            cart = this.getCartByCustomer(loginUser);
        /*    if (isMerge)*/
                return this.mergeCart(cart, items, false);
            /*else
                return this.save(cart, items);*/
        }
        //if (cart.getCartState() == CartState.CLOSED) {
        cart = this.getCartByCustomer(loginUser);
        if (isMerge)
            return this.mergeCart(cart, items, false);
        else
            return this.mergeCart(cart, items, false);
        //}
    }

    private CartDTO mergeCart(ProfileCart cart, List<CartItemDTO> items, Boolean isMerge) {
        List<ProfileCartItem> cartItems = cart.getCartItems();
        //isMerge = false;

        if(items != null) {
            for (CartItemDTO dto : items) {
                ProfileCartItem existing = cartItems != null?cartItems.stream().filter(x -> x.getProductId().equals(dto.getProductId())).findAny().orElse(null):null;
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
                    if (productService.exists(dto.getProductId().toString())) {
                        ProfileCartItem newCartItem = new ProfileCartItem();
                        newCartItem.setProductId(dto.getProductId());
                        newCartItem.setQuantity(dto.getQuantity());
                        cart.addCartItem(newCartItem);

                    }
                }
            }

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

    private CartDTO save(ProfileCart cart, List<CartItemDTO> items) {
        this.setItems(cart, items);
        cart = cartRepository.saveAndFlush(cart);
        cartRepository.refresh(cart);
        return cartMapper.toDto(cart);
    }

    private ProfileCart getCartByCustomer(Customer loginUser) {
        List<ProfileCart> carts = cartRepository.findAll();//cartRepository.findByCustomerAndCartStateOrderByIdDesc(loginUser, CartState.CLAIMED);
        if (carts.size() == 0) {
            ProfileCart cart = new ProfileCart();
            //cart.setCustomer(loginUser);
            cart.setSecureKey(createUIUD());
            cart.setCartState(CartState.CLAIMED);
            return cart;
        }
        return carts.get(0);
    }

    private void setItems(ProfileCart cart, List<CartItemDTO> items) {
        //List<CartItem> cartItems = new ArrayList<>();
        cart.getItems().clear();
        for(CartItemDTO dto : items) {
            Long ref = dto.getProductId();
/*            if(ref != null && productService.exists(ref)) {
                CartItem item = cartItemMapper.toEntity(dto);
                cart.addCartItem(item);
            }*/
        }
        //cart.setCartItems(cartItems);
    }

/*    public String createCheckout(String secureKey, List<CartItemDTO> items) {
        return this.createCheckoutWithCart(secureKey, items).getSecureKey();
    }*/

/*    @Transactional(readOnly = true)
    public CheckoutCart createCheckoutWithCart(String secureKey, List<CartItemDTO> items) {
        //ProfileCart cart = cartRepository.findBySecureKey(secureKey).orElse(new ProfileCart()); //cartMapper.toEntity(cartDTO);
        Customer loginUser = userService.getUserWithAuthorities().orElse(null);
        ProfileCart cart = this.getCartByCustomer(loginUser);

*//*        setItems(cart, items); //cartMapper.toDto(cart);
        cart = cartRepository.save(cart);*//*
        Customer customer = customerRepository.findByIdJoinAddresses(cart.getCustomer().getId()).get();
        cart = cartRepository.getCartByCustomerJoinAddresses(cart.getId());
        List<CartItemInfo> cartItems = cartItemRepository.findCartItemsWithProductNative(cart.getId());

        //cartItems.stream().filter(x->x.getImage() != null && !x.getImage().startsWith("http")).map()


        CheckoutCart checkoutCart = checkoutCartRepository.findBySecureKey(cart.getSecureKey()).orElse(new CheckoutCart());
        checkoutCart.setItems(cartItems.stream().map(checkoutLineItemMapper::cartItemToLineItem).collect(Collectors.toList()));
        if (customer.getAddresses() != null && customer.getAddresses().size() > 0)
            checkoutCart.setAddresses(customer.getAddresses().stream().map(checkoutAddressMapper::addressToAddressPojo).filter(x->x.getPlusCode() != null).collect(Collectors.toList()));
        checkoutCart.setSecureKey(cart.getSecureKey());
        checkoutCart.setName(cart.getCustomer().getFirstname() + " " + cart.getCustomer().getFirstname());
        checkoutCart.setEmail(cart.getCustomer().getEmail());

        checkoutCart = checkoutCartRepository.save(checkoutCart);
        return checkoutCart;
    }*/

    public void closeCart(String secureKey) {
        ProfileCart cart = cartRepository.findBySecureKey(secureKey).orElse(null);
        if(cart == null)
            return;
        cart.setCartState(CartState.CLOSED);
        cartRepository.save(cart);
    }

   public CheckoutCart createCustomCart(CheckoutCart cart) {
        return cart;
   }
}
