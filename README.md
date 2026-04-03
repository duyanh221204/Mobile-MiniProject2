# Fruit App - Android Mini Project

Ung dung Android quan ly ban hang hoa qua su dung Room Database va SharedPreferences.

## Thong tin dang nhap

| Username | Password   |
|----------|------------|
| admin    | admin123   |

## Chuc nang chinh

### 1. Splash Screen
- Hien thi logo va ten ung dung voi hieu ung fade-in
- Tu dong chuyen sang Login hoac Home tuy trang thai dang nhap (SharedPreferences)

### 2. Dang nhap (Login)
- Dang nhap bang username/email va password
- Trang thai dang nhap luu bang SharedPreferences
- Ho tro redirect ve trang truoc (Cart, Orders)

### 3. Trang chu (Home)
- Thanh tim kiem san pham
- Danh sach categories (cuon ngang)
- "Today's Special" - Luoi 2 cot hien thi san pham hom nay
- **San pham het han (expiryDate < hom nay) se tu dong bi an**
- Nhan vao category -> chuyen den danh sach san pham theo category
- Nhan vao san pham -> xem chi tiet

### 4. Danh muc (Categories)
- Hien thi tat ca danh muc dang luoi 2 cot
- Nhan vao danh muc -> hien thi cac san pham thuoc danh muc do (da loc het han)

### 5. Chi tiet san pham (Product Detail)
- Hinh anh, ten, gia, don vi
- **Ngay san xuat (Production Date)** va **Han su dung (Expiry Date)**
- Mo ta san pham
- Chon so luong va them vao gio hang

### 6. Gio hang (Cart)
- Xem danh sach san pham da them
- Tang/giam so luong, xoa san pham
- Dat hang (Place Order)

### 7. Lich su don hang (Orders)
- Xem danh sach don hang da dat
- Xem chi tiet hoa don (Invoice)

### 8. Ho so (Profile)
- Hien thi thong tin nguoi dung: Ho ten, Username, Email, So dien thoai, User ID
- Dang xuat

## Co so du lieu (Room Database)

Gom 5 bang voi cac moi quan he:

```
Products ----n:1---- Categories
Products ----1:n---- OrderDetails
Orders ------1:n---- OrderDetails
Users -------1:n---- Orders
```

### Bang Users
| Column   | Type    | Note         |
|----------|---------|--------------|
| userId   | int     | PK, auto     |
| username | String  |              |
| email    | String  |              |
| password | String  |              |
| fullName | String  |              |
| phone    | String  |              |
| avatar   | String  |              |

### Bang Categories
| Column       | Type   | Note     |
|--------------|--------|----------|
| categoryId   | int    | PK, auto |
| categoryName | String |          |
| imageUrl     | String |          |

### Bang Products
| Column      | Type   | Note                              |
|-------------|--------|-----------------------------------|
| productId   | int    | PK, auto                          |
| productName | String |                                   |
| price       | double |                                   |
| unit        | String | kg, unit                          |
| imageUrl    | String | key map sang drawable             |
| description | String |                                   |
| categoryId  | int    | FK -> Categories                  |
| dateAdded   | String | yyyy-MM-dd, ngay them san pham    |
| expiryDate  | String | yyyy-MM-dd, han su dung           |

### Bang Orders
| Column      | Type   | Note          |
|-------------|--------|---------------|
| orderId     | int    | PK, auto      |
| userId      | int    | FK -> Users   |
| orderDate   | String |               |
| totalAmount | double |               |
| status      | String |               |

### Bang OrderDetails
| Column        | Type   | Note            |
|---------------|--------|-----------------|
| orderDetailId | int    | PK, auto        |
| orderId       | int    | FK -> Orders    |
| productId     | int    | FK -> Products  |
| quantity      | int    |                 |
| unitPrice     | double |                 |

## Cong nghe su dung

- **Ngon ngu**: Java
- **Database**: Room Database (SQLite)
- **Session**: SharedPreferences
- **UI**: Material Design 3, RecyclerView, CardView, BottomNavigationView
- **Min SDK**: 29 (Android 10)
- **Target SDK**: 36

## Cau truc thu muc

```
app/src/main/java/com/duyanhnguyen/miniproject/
|-- SplashActivity.java
|-- LoginActivity.java
|-- MainActivity.java              # Trang chu
|-- CategoryActivity.java          # Danh sach categories
|-- CategoryProductsActivity.java  # San pham theo category
|-- ProductDetailActivity.java     # Chi tiet san pham
|-- CartActivity.java              # Gio hang
|-- OrderHistoryActivity.java      # Lich su don hang
|-- InvoiceDetailActivity.java     # Chi tiet hoa don
|-- ProfileActivity.java           # Ho so nguoi dung
|
|-- adapter/
|   |-- ProductAdapter.java
|   |-- CategoryAdapter.java
|   |-- CategoryGridAdapter.java
|   |-- CartItemAdapter.java
|   |-- OrderHistoryAdapter.java
|
|-- database/
|   |-- AppDatabase.java           # Room DB + seed data
|   |-- dao/
|   |   |-- UserDao.java
|   |   |-- CategoryDao.java
|   |   |-- ProductDao.java
|   |   |-- OrderDao.java
|   |   |-- OrderDetailDao.java
|   |-- entity/
|       |-- User.java
|       |-- Category.java
|       |-- Product.java
|       |-- Order.java
|       |-- OrderDetail.java
|
|-- utils/
    |-- SessionManager.java        # SharedPreferences login
    |-- CartManager.java           # SharedPreferences cart
    |-- ProductImages.java         # Map imageUrl -> drawable
    |-- OrderStatus.java
    |-- OrderDisplay.java
```

## Giao dien

- **Theme**: Trang - Xanh la (#4CAF50)
- **Bottom Navigation**: 5 tab (Home, Categories, Cart, Orders, Profile)
- **San pham**: Hien thi dang luoi 2 cot voi hinh anh vector, gia, don vi, nut Add
- **Category**: Icon vector rieng cho tung loai (Citrus, Berries, Tropical, Melons, Apples)

## Du lieu mau

Khi cai dat lan dau, ung dung tu dong tao:
- 1 tai khoan admin
- 5 danh muc: Citrus, Berries, Tropical, Melons, Apples & Pears
- 8 san pham voi ngay san xuat = hom nay, han su dung = 5-21 ngay sau
