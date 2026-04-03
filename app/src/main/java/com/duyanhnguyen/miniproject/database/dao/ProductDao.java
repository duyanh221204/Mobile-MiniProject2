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

    @Query("SELECT * FROM Products WHERE expiryDate >= :today OR expiryDate IS NULL")
    List<Product> getAllProducts(String today);

    @Query("SELECT * FROM Products WHERE dateAdded = :date AND (expiryDate >= :today OR expiryDate IS NULL)")
    List<Product> getProductsByDate(String date, String today);

    @Query("SELECT * FROM Products WHERE categoryId = :categoryId AND (expiryDate >= :today OR expiryDate IS NULL)")
    List<Product> getProductsByCategory(int categoryId, String today);

    @Query("SELECT * FROM Products WHERE productName LIKE '%' || :keyword || '%' AND (expiryDate >= :today OR expiryDate IS NULL)")
    List<Product> searchProducts(String keyword, String today);

    @Query("SELECT * FROM Products WHERE productId = :id")
    Product getProductById(int id);
}
