package com.badals.shop.service;

import com.badals.shop.domain.Cart;
import com.badals.shop.domain.CartItem;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.enumeration.CartState;
import com.badals.shop.repository.CartRepository;
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

    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    private final UserService userService;

    public static String createUIUD() {
        // Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public CartService(CartRepository cartRepository, CartMapper cartMapper, CartItemMapper cartItemMapper, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
        this.userService = userService;
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

    public CartDTO updateCart(String secureKey, List<CartItemDTO> items) {
        Cart cart = null;
        Customer loginUser = userService.getUserWithAuthorities().orElse(null);

        if(secureKey != null) {
            cart = cartRepository.findBySecureKey(secureKey).orElse(null);
        }

        if(loginUser == null) {
            if (cart == null || cart.getCartState() != CartState.UNCLAIMED) {
                cart = new Cart();
                cart.setSecureKey(createUIUD());
            }
            return this.save(cart, items);
        }

        //logged in user
        if (cart == null || cart.getCartState() == CartState.UNCLAIMED) {
            cart = this.getCartByCustomer(loginUser);
            return this.mergeCart(cart, items);
        }
        if (cart.getCartState() == CartState.CLAIMED) {
            if( loginUser.getId() == cart.getCustomer().getId() )
                return this.save(cart, items);

            cart = this.getCartByCustomer(loginUser);
            return this.mergeCart(cart, items);
        }
        //if (cart.getCartState() == CartState.CLOSED) {
        cart = this.getCartByCustomer(loginUser);
        return this.mergeCart(cart, items);
        //}
    }

    private CartDTO mergeCart(Cart cart, List<CartItemDTO> items) {
        List<CartItem> cartItems = cart.getCartItems();

        for(CartItemDTO dto : items) {
            CartItem existing = cartItems.stream().filter(x -> x.getProductId() == dto.getProductId()).findAny().orElse(null);
            if(existing != null)
                existing.setQuantity(existing.getQuantity() + dto.getQuantity());
            else
                cart.addCartItem(cartItemMapper.toEntity(dto));
        }
        cart = cartRepository.save(cart);
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
        cart = cartRepository.save(cart);
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
        List<CartItem> cartItems = new ArrayList<>();
        for(CartItemDTO dto : items) {
            CartItem item = cartItemMapper.toEntity(dto);
            item.setCart(cart);
            cartItems.add(cartItemMapper.toEntity(dto));
        }
        cart.setCartItems(cartItems);
    }
}
