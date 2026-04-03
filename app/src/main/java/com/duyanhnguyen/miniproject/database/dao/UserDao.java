package com.duyanhnguyen.miniproject.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.duyanhnguyen.miniproject.database.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM Users WHERE (username = :usernameOrEmail OR email = :usernameOrEmail) AND password = :password LIMIT 1")
    User login(String usernameOrEmail, String password);

    @Query("SELECT * FROM Users WHERE userId = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM Users")
    List<User> getAllUsers();

    @Query("SELECT COUNT(*) FROM Users")
    int getUserCount();
}
