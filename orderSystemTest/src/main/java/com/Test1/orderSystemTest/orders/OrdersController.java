package com.Test1.orderSystemTest.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final JdbcClientOrdersRepository ordersRepository;
    public OrdersController(JdbcClientOrdersRepository ordersRepository){
        this.ordersRepository = ordersRepository;
    }

    @GetMapping("")
    List<Orders> findAll(){
        return ordersRepository.findAll();
    }

    @GetMapping("/{id}")
    Orders finById(@PathVariable Integer id){
        Optional<Orders> orders = ordersRepository.findById(id);
        if(orders.isEmpty()){
            throw new OrdersNotFoundExpection();
        }
        return orders.get();
    }

//    @GetMapping("/{name}")
//    List<Orders> findByCustomerName(@PathVariable String customerName){
//        return ordersRepository.findByCustomerName(customerName);
//    }
//
//    @GetMapping("/{number}")
//    List<Orders> findByCustomerNumber(@PathVariable String customerNumber){
//        return ordersRepository.findByCustomerNumber(customerNumber);
//    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/created")
    void create(@RequestBody Orders orders){
        ordersRepository.create(orders);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody Orders orders, @PathVariable Integer id){
        ordersRepository.update(orders,id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id){
        ordersRepository.delete(id);
    }



}
