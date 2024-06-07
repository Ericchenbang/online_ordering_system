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
public class JdbcClientMenuRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcClientOrdersRepository.class);
    private final JdbcClient jdbcClient;

    public JdbcClientMenuRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;

    }

    public List<Menu> findAll(){
        return jdbcClient.sql("SELECT *FROM MENU")
                .query((rs, rowNum) -> new Menu(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getString("category")
                ))
                .list();
    }

    public List<Menu> findByCategory(String category){
        return jdbcClient.sql("SELECT id,item_name,price,category FROM MENU WHERE category = :category" )
                .param("category", category)
                .query((rs, rowNum) -> new Menu(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getString("category")
                ))
                .list();
    }

    public List<Menu> findByItemName(String itemName){
        return jdbcClient.sql("SELECT id,item_name,price,category FROM MENU WHERE item_name = :itemName" )
                .param("item_name", itemName)
                .query((rs, rowNum) -> new Menu(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getString("category")
                ))
                .list();
    }

    public Integer findItemIdByName(String name) {
        return jdbcClient.sql("SELECT id FROM MENU WHERE item_name = :item_name")
                .param("item_name", name)
                .query((rs, rowNum) -> rs.getInt("id"))
                .single();
    }

    public void addMenuItem(Menu menu) {
        // Retrieve the current max id from the Orders table
        Integer maxMenuId = jdbcClient.sql("SELECT COALESCE(MAX(id), 0) FROM MENU")
                .query((rs, rowNum) -> rs.getInt(1))
                .single();

        Integer newMenuId = maxMenuId + 1;

        // Insert into Menu table
        var updated = jdbcClient.sql("INSERT INTO MENU(id, item_name, price, category) values(?,?,?,?)")
                .params(List.of(newMenuId, menu.itemName(), menu.price(), menu.category()))
                .update();

        Assert.state(updated == 1, "Failed to create orders " + menu.itemName());

    }

    public void update(Menu menu, Integer id) {
        deleteMenuItemById(id);

        addMenuItem(menu);

        //Assert.state(updated == 1, "Failed to update orders " + menu.itemName());
    }

    public void deleteMenuItemById(int id) {
        var updated = jdbcClient.sql("DELETE FROM MENU WHERE id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Failed to delete id " + id);
    }

}
