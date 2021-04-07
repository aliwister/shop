package com.badals.shop.service;

import com.badals.shop.domain.Cart;
import com.badals.shop.domain.CartItem;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.domain.checkout.helper.CheckoutAddressMapper;
import com.badals.shop.domain.checkout.helper.CheckoutCartMapper;
import com.badals.shop.domain.checkout.helper.CheckoutLineItemMapper;
import com.badals.shop.repository.*;
import com.badals.shop.domain.enumeration.CartState;
import com.badals.shop.repository.projection.CartItemInfo;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.badals.shop.service.mapper.CartItemMapper;
import com.badals.shop.service.mapper.CartMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Cart}.
 */
@Service
@Transactional
public class CartService {

    private final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;

    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    private final CheckoutCartMapper checkoutCartMapper;
    private final CheckoutLineItemMapper checkoutLineItemMapper;
    private final CheckoutCartRepository checkoutCartRepository;
    private final CheckoutAddressMapper checkoutAddressMapper;

    private final UserService userService;
    private final ProductService productService;

    public static String createUIUD() {
        // Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, CustomerRepository customerRepository, CartMapper cartMapper, CartItemMapper cartItemMapper, CheckoutCartRepository checkoutCartRepository, CheckoutCartMapper checkoutCartMapper, UserService userService, ProductService productService, CheckoutLineItemMapper checkoutLineItemMapper, CheckoutAddressMapper checkoutAddressMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
        this.userService = userService;
        this.checkoutCartMapper = checkoutCartMapper;
        this.checkoutCartRepository = checkoutCartRepository;
        this.productService = productService;
        this.checkoutLineItemMapper = checkoutLineItemMapper;
        this.checkoutAddressMapper = checkoutAddressMapper;
    }

    /**
     * Save a cart.
     *
     * @param cartDTO the entity to save.
     * @return the persisted entity.
     */
    public CartDTO save(CartDTO cartDTO) {
        log.debug("Request to save Cart : {}", cartDTO);
        Cart cart = cartMapper.toEntity(cartDTO);
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
        log.debug("Request to save Cart : {}", cartDTO);
        Cart cart = cartMapper.toEntity(cartDTO);
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
        log.debug("Request to get Cart : {}", id);
        return cartRepository.findById(id)
            .map(cartMapper::toDto);
    }

    /**
     * Delete the cart by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cart : {}", id);
        cartRepository.deleteById(id);
    }

    public CartDTO updateCart(String secureKey, List<CartItemDTO> items, boolean isMerge) {
        Cart cart = null;
        Customer loginUser = userService.getUserWithAuthorities().orElse(null);
        log.info("Logged in user " + loginUser);
        // secureKey always null
        if(secureKey != null) {
            cart = cartRepository.findBySecureKey(secureKey).orElse(null);
        }

        // Never get called if user not logged in
        if(loginUser == null) {
            if (cart == null || cart.getCartState() != CartState.UNCLAIMED) {
                cart = new Cart();
                cart.setSecureKey(createUIUD());
            }
            return this.mergeCart(cart, items, false);
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

    private CartDTO mergeCart(Cart cart, List<CartItemDTO> items, Boolean isMerge) {
        List<CartItem> cartItems = cart.getCartItems();
        isMerge = false;

        if(items != null) {
            for (CartItemDTO dto : items) {
                CartItem existing = cartItems.stream().filter(x -> x.getProductId().equals(dto.getProductId())).findAny().orElse(null);
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
                    //Product p = productRepository.findOneByRef(dto.getProductId()).orElse(null);
                    if (productService.exists(dto.getProductId())) {
                        CartItem newCartItem = cartItemMapper.toEntity(dto);
                        newCartItem.setProductId(dto.getProductId());
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

    private CartDTO save(Cart cart, List<CartItemDTO> items) {
        this.setItems(cart, items);
        cart = cartRepository.saveAndFlush(cart);
        cartRepository.refresh(cart);
        return cartMapper.toDto(cart);
    }

    private Cart getCartByCustomer(Customer loginUser) {
        List<Cart> carts = cartRepository.findByCustomerAndCartStateOrderByIdDesc(loginUser, CartState.CLAIMED);
        if (carts.size() == 0) {
            Cart cart = new Cart();
            cart.setCustomer(loginUser);
            cart.setSecureKey(createUIUD());
            cart.setCartState(CartState.CLAIMED);
            return cart;
        }
        return carts.get(0);
    }

    private void setItems(Cart cart, List<CartItemDTO> items) {
        //List<CartItem> cartItems = new ArrayList<>();
        cart.getCartItems().clear();;
        for(CartItemDTO dto : items) {
            Long ref = dto.getProductId();
            if(ref != null && productService.exists(ref)) {
                CartItem item = cartItemMapper.toEntity(dto);
                cart.addCartItem(item);
            }
        }
        //cart.setCartItems(cartItems);
    }

    public String createCheckout(String secureKey, List<CartItemDTO> items) {
        return this.createCheckoutWithCart(secureKey, items).getSecureKey();
    }

    @Transactional(readOnly = true)
    public CheckoutCart createCheckoutWithCart(String secureKey, List<CartItemDTO> items) {
        //Cart cart = cartRepository.findBySecureKey(secureKey).orElse(new Cart()); //cartMapper.toEntity(cartDTO);
        Customer loginUser = userService.getUserWithAuthorities().orElse(null);
        Cart cart = this.getCartByCustomer(loginUser);

/*        setItems(cart, items); //cartMapper.toDto(cart);
        cart = cartRepository.save(cart);*/
        Customer customer = customerRepository.findByIdJoinAddresses(cart.getCustomer().getId()).get();
        cart = cartRepository.getCartByCustomerJoinAddresses(cart.getId());
        List<CartItemInfo> cartItems = cartItemRepository.findCartItemsWithProductNative(cart.getId());


        CheckoutCart checkoutCart = checkoutCartRepository.findBySecureKey(cart.getSecureKey()).orElse(new CheckoutCart());
        checkoutCart.setItems(cartItems.stream().map(checkoutLineItemMapper::cartItemToLineItem).collect(Collectors.toList()));
        if (customer.getAddresses() != null && customer.getAddresses().size() > 0)
            checkoutCart.setAddresses(customer.getAddresses().stream().map(checkoutAddressMapper::addressToAddressPojo).filter(x->x.getPlusCode() != null).collect(Collectors.toList()));
        checkoutCart.setSecureKey(cart.getSecureKey());
        checkoutCart.setName(cart.getCustomer().getFirstname() + " " + cart.getCustomer().getFirstname());
        checkoutCart.setEmail(cart.getCustomer().getEmail());

        checkoutCart = checkoutCartRepository.save(checkoutCart);
        return checkoutCart;
    }

    public void closeCart(String secureKey) {
        Cart cart = cartRepository.findBySecureKey(secureKey).orElse(null);
        if(cart == null)
            return;
        cart.setCartState(CartState.CLOSED);
        cartRepository.save(cart);
    }

   public CheckoutCart createCustomCart(CheckoutCart cart) {
        return cart;
   }
}
