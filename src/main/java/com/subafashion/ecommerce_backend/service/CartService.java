package com.subafashion.ecommerce_backend.service;

import com.subafashion.ecommerce_backend.dto.AddToCartRequest;
import com.subafashion.ecommerce_backend.dto.CartResponse;
import com.subafashion.ecommerce_backend.model.Cart;
import com.subafashion.ecommerce_backend.model.CartItem;
import com.subafashion.ecommerce_backend.model.Product;
import com.subafashion.ecommerce_backend.model.User;
import com.subafashion.ecommerce_backend.repository.CartItemRepository;
import com.subafashion.ecommerce_backend.repository.CartRepository;
import com.subafashion.ecommerce_backend.repository.ProductRepository;
import com.subafashion.ecommerce_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    private Cart getOrCreateCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public CartResponse getCart(String email) {
        Cart cart = getOrCreateCart(email);
        return mapToResponse(cart);
    }

    public CartResponse addToCart(String email, AddToCartRequest request) {
        Cart cart = getOrCreateCart(email);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Not enough stock available!");
        }

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    return newItem;
                });
        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItem);

        return mapToResponse(getOrCreateCart(email));
    }

    public CartResponse updateQuantity(String email, Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return mapToResponse(getOrCreateCart(email));
    }

    public CartResponse removeFromCart(String email, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemRepository.delete(cartItem);
        return mapToResponse(getOrCreateCart(email));
    }

    public void clearCart(String email) {
        Cart cart = getOrCreateCart(email);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private CartResponse mapToResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());

        List<CartResponse.CartItemResponse> itemResponses = cart.getItems()
                .stream()
                .map(item -> {
                    CartResponse.CartItemResponse ir = new CartResponse.CartItemResponse();
                    ir.setCartItemId(item.getId());
                    ir.setProductId(item.getProduct().getId());
                    ir.setProductName(item.getProduct().getName());
                    ir.setProductImage(item.getProduct().getImageUrl());
                    ir.setProductPrice(item.getProduct().getPrice());
                    ir.setQuantity(item.getQuantity());
                    ir.setSubtotal(item.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity())));
                    return ir;
                })
                .collect(Collectors.toList());

        response.setItems(itemResponses);
        response.setTotalItems(itemResponses.stream()
                .mapToInt(CartResponse.CartItemResponse::getQuantity).sum());
        response.setTotalPrice(itemResponses.stream()
                .map(CartResponse.CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return response;
    }
}
