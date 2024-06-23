package com.Test1.orderSystemTest.menu;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

public record Menu(
        @Id
        //@GeneratedValue(strategy = GenerationType.IDENTITY)
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
