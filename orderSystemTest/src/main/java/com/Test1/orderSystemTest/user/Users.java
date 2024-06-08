package com.Test1.orderSystemTest.user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

public record Users(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer userId,
        String userName,
        String userPhoneNumber
) {

    public Users{
        if (userName.isEmpty()){
            throw new IllegalArgumentException("User name must not be empty.");
        }
        if (userPhoneNumber.isEmpty()){
            throw new IllegalArgumentException("User phone number must not be empty.");
        }
    }
}
