package com.example.quanlykhohang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// lớp lo phần database, kế thừa SQLiteOpenHelper
// database tên warehouse.db, version 1
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "warehouse.db";
    public static final int DB_VERSION = 1;

    // bảng users
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // bảng products
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_CATEGORY = "category";
    public static final String COL_ACTIVE = "active";
    public static final String COL_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // tạo bảng users trước
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " TEXT NOT NULL UNIQUE, "
                + COL_PASSWORD + " TEXT NOT NULL)");

        // rồi tạo bảng products
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_PRICE + " INTEGER NOT NULL, "
                + COL_QUANTITY + " INTEGER NOT NULL, "
                + COL_CATEGORY + " TEXT NOT NULL, "
                + COL_ACTIVE + " INTEGER NOT NULL, "
                + COL_DESCRIPTION + " TEXT)");

        // thêm sẵn tài khoản admin để đăng nhập test
        ContentValues admin = new ContentValues();
        admin.put(COL_USERNAME, "admin");
        admin.put(COL_PASSWORD, "123456");
        db.insert(TABLE_USERS, null, admin);

        // thêm 3 sản phẩm mẫu cho có dữ liệu
        insertSampleProduct(db, "Chuột không dây", 150000, 20, "Điện tử", 1, "");
        insertSampleProduct(db, "Bàn phím cơ", 450000, 3, "Điện tử", 1, "");
        insertSampleProduct(db, "Sổ tay A5", 25000, 40, "Văn phòng phẩm", 1, "");
    }

    // hàm phụ để thêm sản phẩm mẫu cho gọn
    private void insertSampleProduct(SQLiteDatabase db, String name, int price,
                                     int quantity, String category, int active,
                                     String description) {
        ContentValues v = new ContentValues();
        v.put(COL_NAME, name);
        v.put(COL_PRICE, price);
        v.put(COL_QUANTITY, quantity);
        v.put(COL_CATEGORY, category);
        v.put(COL_ACTIVE, active);
        v.put(COL_DESCRIPTION, description);
        db.insert(TABLE_PRODUCTS, null, v);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nâng version thì xóa làm lại cho nhanh
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // kiểm tra đăng nhập, đúng thì trả về true
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS,
                    new String[]{COL_USER_ID},
                    COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                    new String[]{username, password},
                    null, null, null);
            return cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public long insertUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_USERNAME, username);
        v.put(COL_PASSWORD, password);
        return db.insert(TABLE_USERS, null, v);
    }

    // thêm sản phẩm mới, trả về id
    public long insertProduct(Product p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_NAME, p.getName());
        v.put(COL_PRICE, p.getPrice());
        v.put(COL_QUANTITY, p.getQuantity());
        v.put(COL_CATEGORY, p.getCategory());
        v.put(COL_ACTIVE, p.getActive());
        v.put(COL_DESCRIPTION, p.getDescription());
        return db.insert(TABLE_PRODUCTS, null, v);
    }

    // lấy hết sản phẩm để hiện lên list, mới nhất lên đầu
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_PRODUCTS, null,
                    null, null, null, null, COL_ID + " DESC");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(cursorToProduct(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    // lấy 1 sản phẩm theo id, không có thì trả về null
    public Product getProductById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_PRODUCTS, null,
                    COL_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursorToProduct(cursor);
            }
            return null;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    // cập nhật số lượng, dùng cho nút nhập thêm / bán
    public int updateQuantity(int id, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_QUANTITY, quantity);
        return db.update(TABLE_PRODUCTS, v,
                COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteProduct(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PRODUCTS,
                COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    // tính tổng tiền kho = cộng hết giá * số lượng
    public long getTotalValue() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT SUM(" + COL_PRICE + " * " + COL_QUANTITY + ") FROM " + TABLE_PRODUCTS,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.isNull(0) ? 0 : cursor.getLong(0);
            }
            return 0;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    // đếm mấy món sắp hết (tồn < 5)
    public int countLowStock() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT COUNT(*) FROM " + TABLE_PRODUCTS + " WHERE " + COL_QUANTITY + " < 5",
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    private Product cursorToProduct(Cursor c) {
        return new Product(
                c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                c.getInt(c.getColumnIndexOrThrow(COL_PRICE)),
                c.getInt(c.getColumnIndexOrThrow(COL_QUANTITY)),
                c.getString(c.getColumnIndexOrThrow(COL_CATEGORY)),
                c.getInt(c.getColumnIndexOrThrow(COL_ACTIVE)),
                c.getString(c.getColumnIndexOrThrow(COL_DESCRIPTION)));
    }
}
