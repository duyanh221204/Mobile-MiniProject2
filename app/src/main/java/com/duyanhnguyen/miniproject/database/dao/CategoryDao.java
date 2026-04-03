package com.duyanhnguyen.miniproject.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.duyanhnguyen.miniproject.database.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    long insert(Category category);

    @Query("SELECT * FROM Categories")
    List<Category> getAllCategories();

    @Query("SELECT * FROM Categories WHERE categoryId = :id")
    Category getCategoryById(int id);
}
