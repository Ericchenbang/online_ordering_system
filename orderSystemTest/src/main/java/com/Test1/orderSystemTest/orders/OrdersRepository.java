package com.Test1.orderSystemTest.orders;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;


public interface OrdersRepository extends ListCrudRepository<Orders ,Long> {

}
