package com.Test1.orderSystemTest.user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

public record Users(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer userId,
        String userName,
        String userPhoneNumber,
        String userPassword
) {

    public Users{
        if (userName.isEmpty()){
            throw new IllegalArgumentException("User name must not be empty.");
        }
        if (userPhoneNumber.isEmpty()){
            throw new IllegalArgumentException("User phone number must not be empty.");
        }
        if (userPassword.isEmpty()){
            throw new IllegalArgumentException("User password must not be empty.");
        }
    }
}
