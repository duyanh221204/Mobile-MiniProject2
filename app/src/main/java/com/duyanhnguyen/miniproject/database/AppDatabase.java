package com.duyanhnguyen.miniproject.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.duyanhnguyen.miniproject.database.dao.CategoryDao;
import com.duyanhnguyen.miniproject.database.dao.OrderDao;
import com.duyanhnguyen.miniproject.database.dao.OrderDetailDao;
import com.duyanhnguyen.miniproject.database.dao.ProductDao;
import com.duyanhnguyen.miniproject.database.dao.UserDao;
import com.duyanhnguyen.miniproject.database.entity.Category;
import com.duyanhnguyen.miniproject.database.entity.Order;
import com.duyanhnguyen.miniproject.database.entity.OrderDetail;
import com.duyanhnguyen.miniproject.database.entity.Product;
import com.duyanhnguyen.miniproject.database.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Category.class, Product.class, Order.class, OrderDetail.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailDao orderDetailDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "fruit_app_db")
                            .allowMainThreadQueries()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        AppDatabase database = getInstance(context);
                                        seedData(database);
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void seedData(AppDatabase db) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Seed user: admin/admin123
        User admin = new User("admin", "admin@fruitapp.com", "admin123", "Admin User", "0123456789");
        db.userDao().insert(admin);

        // Seed categories
        Category citrus = new Category("Citrus", "citrus");
        Category berries = new Category("Berries", "berries");
        Category tropical = new Category("Tropical", "tropical");
        Category melons = new Category("Melons", "melons");
        Category apples = new Category("Apples & Pears", "apples");

        db.categoryDao().insert(citrus);
        db.categoryDao().insert(berries);
        db.categoryDao().insert(tropical);
        db.categoryDao().insert(melons);
        db.categoryDao().insert(apples);

        // Seed products (today's specials)
        db.productDao().insert(new Product("Ruby Red Grapefruit", 7.00, "kg", "grapefruit", "Fresh ruby red grapefruit", 1, today));
        db.productDao().insert(new Product("Organic Blueberries", 5.00, "unit", "blueberries", "Organic fresh blueberries", 2, today));
        db.productDao().insert(new Product("Sweet Mangoes", 1.00, "kg", "mango", "Sweet ripe mangoes", 3, today));
        db.productDao().insert(new Product("Honeycrisp Apples", 7.00, "unit", "apple", "Crispy and sweet apples", 5, today));
        db.productDao().insert(new Product("Fresh Oranges", 3.50, "kg", "orange", "Juicy fresh oranges", 1, today));
        db.productDao().insert(new Product("Strawberries", 4.00, "unit", "strawberry", "Sweet fresh strawberries", 2, today));
        db.productDao().insert(new Product("Ripe Bananas", 2.00, "kg", "banana", "Perfectly ripe bananas", 3, today));
        db.productDao().insert(new Product("Watermelon", 6.00, "unit", "watermelon", "Sweet juicy watermelon", 4, today));
    }
}
