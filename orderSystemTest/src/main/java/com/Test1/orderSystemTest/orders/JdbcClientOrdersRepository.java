package com.Test1.orderSystemTest.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcClientOrdersRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcClientOrdersRepository.class);
    private final JdbcClient jdbcClient;

    public JdbcClientOrdersRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;

    }

    public List<Orders> findAll(){
        return jdbcClient.sql("select * from ORDERS")
                .query((rs, rowNum) -> new Orders(
                        rs.getInt("id"),
                        rs.getTimestamp("order_time"),
                        rs.getString("customer_name"),
                        rs.getString("customer_number"),
                        rs.getDouble("total_price"),
                        rs.getString("status"),
                        findOrderItemsByOrderId(rs.getInt("id"))
                ))
                .list();
    }

    public Optional<Orders> findById(Integer id) {
        return jdbcClient.sql("SELECT id,order_time,customer_name,customer_number,total_price,status FROM ORDERS WHERE id = :id" )
                .param("id", id)
                .query((rs, rowNum) -> new Orders(
                        rs.getInt("id"),
                        rs.getTimestamp("order_time"),
                        rs.getString("customer_name"),
                        rs.getString("customer_number"),
                        rs.getDouble("total_price"),
                        rs.getString("status"),
                        findOrderItemsByOrderId(rs.getInt("id"))
                ))
                .optional();
    }

    private List<OrderItems> findOrderItemsByOrderId(Integer orderId) {
        return jdbcClient.sql("SELECT * FROM ORDER_ITEMS WHERE order_id = :order_id")
                .param("order_id", orderId)
                .query((rs, rowNum) -> new OrderItems(
                        rs.getInt("order_id"),
                        rs.getInt("menu_item_id"),
                        rs.getInt("quantity")
                ))
                .list();
    }

    public void create(Orders orders) {

        // Retrieve the current max id from the Orders table
        Integer maxOrderId = jdbcClient.sql("SELECT COALESCE(MAX(id), 0) FROM orders")
                .query((rs, rowNum) -> rs.getInt(1))
                .single();

        Integer newOrderId = maxOrderId + 1;

        // Insert into Orders table
        var updated = jdbcClient.sql("INSERT INTO ORDERS(id, order_time,customer_name,customer_number,total_price,status) values(?,?,?,?,?,?)")
                .params(List.of(newOrderId,Timestamp.valueOf(LocalDateTime.now()),orders.customerName(),orders.customerNumber(),orders.totalPrice(),"Pending"))
                .update();

        Assert.state(updated == 1, "Failed to create orders " + orders.customerName());

        // Insert into OrderItems
        for (OrderItems item : orders.items()) {
            jdbcClient.sql("INSERT INTO ORDER_ITEMS (order_id, menu_item_id, quantity) VALUES (?, ?, ?)")
                    .params(List.of(newOrderId, item.menuItemId(), item.quantity()))
                    .update();
        }
    }

    public void update(Orders orders, Integer id) {
        var updated = jdbcClient.sql("UPDATE ORDERS set order_time = ?, customer_name = ?, customer_number = ?, total_price = ?, status = ? where id =:id")
                .params(List.of(orders.orderTime(),orders.customerName(),orders.customerNumber(), orders.totalPrice(),orders.status()))
                .update();

        Assert.state(updated == 1, "Failed to update orders " + orders.customerName());

        // Delete old OrderItems
        jdbcClient.sql("DELETE FROM ORDER_ITEMS WHERE order_id = :order_id")
                .param("order_id", id)
                .update();

        // Insert updated OrderItems
        for (OrderItems item : orders.items()) {
            jdbcClient.sql("INSERT INTO ORDER_ITEMS (order_id, menu_item_id, quantity) VALUES (?, ?, ?)")
                    .params(List.of(id, item.menuItemId(), item.quantity()))
                    .update();
        }
    }

    public void delete(Integer id) {
        // Delete OrderItems first
        jdbcClient.sql("DELETE FROM ORDER_ITEMS WHERE order_id = :order_id")
                .param("order_id", id)
                .update();

        // Then delete Orders
        var updated = jdbcClient.sql("delete from ORDERS where id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Failed to delete order " + id);
    }

    public int count() {
        return jdbcClient.sql("select * from ORDERS").query().listOfRows().size();
    }

    public void saveAll(List<Orders> orders) {
        orders.stream().forEach(this::create);
    }

//    public List<Orders> findByCustomerName(String customerName) {
//        return jdbcClient.sql("select * from ORDERS where customer_name = :customerName")
//                .param("customer_name", customerName)
//                .query(Orders.class)
//                .list();
//    }
//
//    public List<Orders> findByCustomerNumber(String customerNumber) {
//        return jdbcClient.sql("select * from ORDERS where customer_number = :customerNumber")
//                .param("customer_number", customerNumber)
//                .query(Orders.class)
//                .list();
//    }



}
