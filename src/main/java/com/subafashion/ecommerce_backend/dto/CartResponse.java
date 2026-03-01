package com.subafashion.ecommerce_backend.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {

    private Long cartId;
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;
    private Integer totalItems;

    @Data
    public static class CartItemResponse {
        private Long cartItemId;
        private Long productId;
        private String productName;
        private String productImage;
        private BigDecimal productPrice;
        private Integer quantity;
        private BigDecimal subtotal;
    }
}
