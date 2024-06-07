package com.Test1.orderSystemTest.orders;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrdersNotFoundExpection extends RuntimeException{
    public OrdersNotFoundExpection(){
        super("Order Not Found");
    }
}
