package com.duyanhnguyen.miniproject.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.duyanhnguyen.miniproject.database.entity.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Query("SELECT * FROM Orders WHERE userId = :userId ORDER BY orderId DESC")
    List<Order> getOrdersByUser(int userId);

    @Query("SELECT * FROM Orders WHERE orderId = :orderId LIMIT 1")
    Order getOrderById(int orderId);

    @Query("SELECT * FROM Orders")
    List<Order> getAllOrders();
}
