package com.duyanhnguyen.miniproject.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.duyanhnguyen.miniproject.database.entity.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    long insert(Product product);

    @Query("SELECT * FROM Products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM Products WHERE dateAdded = :date")
    List<Product> getProductsByDate(String date);

    @Query("SELECT * FROM Products WHERE categoryId = :categoryId")
    List<Product> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM Products WHERE productName LIKE '%' || :keyword || '%'")
    List<Product> searchProducts(String keyword);

    @Query("SELECT * FROM Products WHERE productId = :id")
    Product getProductById(int id);
}
