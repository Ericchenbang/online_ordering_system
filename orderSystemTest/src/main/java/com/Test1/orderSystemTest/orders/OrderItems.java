package com.Test1.orderSystemTest.orders;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

public record OrderItems(
        @Id
        @Column(name = "order_id")
        Integer orderId,
        @Column(name = "menu_item_id")
        Integer menuItemId,

        Integer quantity
) {
    public OrderItems {
//        if (orderId == null || orderId <= 0) {
//            throw new IllegalArgumentException("Order ID must be positive.");
//        }
//        if (menuItemId == null || menuItemId <= 0) {
//            throw new IllegalArgumentException("Menu item ID must be positive.");
//        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
    }
}
