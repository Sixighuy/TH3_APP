# Ứng dụng Quản lý kho hàng (Android - Java + XML + SQLite)

Bài thực hành môn Lập trình ứng dụng cho thiết bị di động.

## Tài khoản mẫu

- Tên đăng nhập: `admin`
- Mật khẩu: `123456`

## Tính năng

- **Đăng nhập**: kiểm tra bảng `users`, đúng thì sang màn hình chính, nút Back không quay lại màn hình đăng nhập.
- **Trang chính**: thống kê (tổng số sản phẩm, tổng giá trị kho, sắp hết hàng), ListView sản phẩm, cảnh báo số lượng < 5, nhấn dòng mở chi tiết.
- **Thêm sản phẩm**: nhập tên, giá (> 0), số lượng (>= 0), Spinner loại, Switch trạng thái, mô tả; kiểm tra hợp lệ rồi lưu vào SQLite.
- **Chi tiết sản phẩm**: nhận id qua Intent, truy vấn lại theo id; Nhập thêm 1 / Bán 1 / Xóa / Quay lại.

## Dữ liệu mẫu

- 3 sản phẩm: Chuột không dây (150.000đ, tồn 20), Bàn phím cơ (450.000đ, tồn 3), Sổ tay A5 (25.000đ, tồn 40).
- Dữ liệu: SQLite duy nhất; ArrayList chỉ chứa kết quả truy vấn để hiển thị; dùng `?` + selectionArgs; đóng Cursor; tải lại ở `onResume`.

## Mở trong Android Studio

1. Android Studio > Open > chọn thư mục `quan-ly-kho-hang`.
2. Đợi Gradle sync, Run trên emulator hoặc máy thật.
3. Đăng nhập: admin / 123456.

## Cấu trúc thư mục

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/quanlykhohang/
│   ├── LoginActivity.java
│   ├── MainActivity.java
│   ├── AddProductActivity.java
│   ├── ProductDetailActivity.java
│   ├── Product.java
│   ├── ProductAdapter.java
│   └── DatabaseHelper.java
└── res/
    ├── layout/ (activity_login, activity_main, item_product,
    │            activity_add_product, activity_product_detail)
    ├── values/ (strings, colors, themes)
    └── drawable/ (bg_card, bg_input, bg_header)
```
