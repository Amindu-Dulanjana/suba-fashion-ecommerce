package com.subafashion.ecommerce_backend.controller;

import com.subafashion.ecommerce_backend.dto.OrderResponse;
import com.subafashion.ecommerce_backend.dto.PlaceOrderRequest;
import com.subafashion.ecommerce_backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(Authentication auth,
                                                    @Valid @RequestBody PlaceOrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(auth.getName(), request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.getMyOrders(auth.getName()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(Authentication auth,
                                                      @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(auth.getName(), orderId));
    }
}
