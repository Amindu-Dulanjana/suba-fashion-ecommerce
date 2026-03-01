package com.subafashion.ecommerce_backend.controller;

import com.subafashion.ecommerce_backend.dto.AddToCartRequest;
import com.subafashion.ecommerce_backend.dto.CartResponse;
import com.subafashion.ecommerce_backend.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication auth) {
        return ResponseEntity.ok(cartService.getCart(auth.getName()));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(Authentication auth,
                                                  @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(auth.getName(), request));
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartResponse> updateQuantity(Authentication auth,
                                                       @PathVariable Long cartItemId,
                                                       @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(auth.getName(), cartItemId, quantity));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<CartResponse> removeFromCart(Authentication auth,
                                                       @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeFromCart(auth.getName(), cartItemId));
    }
}
