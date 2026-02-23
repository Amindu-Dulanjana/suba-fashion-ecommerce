package com.subafashion.ecommerce_backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private String category;
    private Boolean featured;
    private Boolean active;
    private LocalDateTime createdAt;

    public String getStockStatus() {
        if (stock == 0) return "OUT_OF_STOCK";
        if (stock < 10) return "LOW_STOCK";
        return "IN_STOCK";
    }
}
