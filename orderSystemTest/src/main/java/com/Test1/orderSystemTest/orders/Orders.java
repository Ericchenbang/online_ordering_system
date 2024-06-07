package com.Test1.orderSystemTest.orders;

import org.springframework.data.annotation.Id;
import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


public record Orders(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id,
        Timestamp orderTime,
        String customerName,
        String customerNumber,
        Double totalPrice,
        String status,
        @OneToMany
        @MappedCollection(idColumn = "order_id")
        List<OrderItems> items
) {
    public Orders{
        if (customerName.isEmpty()) {
            throw new IllegalArgumentException("Customer name must not be empty.");
        }
        if (customerNumber.isEmpty()) {
            throw new IllegalArgumentException("Customer phone number must not be empty.");
        }
        if (totalPrice < 0){
            throw new IllegalArgumentException("Total price must be non-negative.");
        }
//        if (status.isEmpty()){
//            throw new IllegalArgumentException("Order status must not be empty.");
//        }
//        if(items.isEmpty()){
//            throw new IllegalArgumentException("Order items must not be empty.");
//        }
    }
}