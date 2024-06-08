package com.Test1.orderSystemTest.menu;

import org.springframework.data.annotation.Id;

public record Menu(
        @Id
        Integer id,
        String itemName,
        //String description,
        Double price,
        String category
) {
    public Menu {
        if (itemName.isEmpty()) {
            throw new IllegalArgumentException("Item name must not be empty.");
        }
//        if (description.isEmpty()) {
//            throw new IllegalArgumentException("Description must not be empty.");
//        }
        if (price < 0) {
            throw new IllegalArgumentException("Price must be non-negative.");
        }
        if (category.isEmpty()){
            throw new IllegalArgumentException("Item category must not be empty.");
        }
    }
}
