package com.duyanhnguyen.miniproject.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.duyanhnguyen.miniproject.database.entity.OrderDetail;

import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert
    long insert(OrderDetail orderDetail);

    @Query("SELECT * FROM OrderDetails WHERE orderId = :orderId")
    List<OrderDetail> getOrderDetailsByOrderId(int orderId);
}
