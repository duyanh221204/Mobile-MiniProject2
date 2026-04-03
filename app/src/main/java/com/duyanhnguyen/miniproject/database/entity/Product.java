package com.duyanhnguyen.miniproject.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Products",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "categoryId",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("categoryId"))
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int productId;
    private String productName;
    private double price;
    private String unit;
    private String imageUrl;
    private String description;
    private int categoryId;
    private String dateAdded;
    private String expiryDate;

    public Product() {}

    public Product(String productName, double price, String unit, String imageUrl,
                   String description, int categoryId, String dateAdded, String expiryDate) {
        this.productName = productName;
        this.price = price;
        this.unit = unit;
        this.imageUrl = imageUrl;
        this.description = description;
        this.categoryId = categoryId;
        this.dateAdded = dateAdded;
        this.expiryDate = expiryDate;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getDateAdded() { return dateAdded; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}
