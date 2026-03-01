package com.subafashion.ecommerce_backend.repository;

import com.subafashion.ecommerce_backend.model.Cart;
import com.subafashion.ecommerce_backend.model.CartItem;
import com.subafashion.ecommerce_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
