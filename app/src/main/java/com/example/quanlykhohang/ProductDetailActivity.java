package com.example.quanlykhohang;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends Activity {

    private TextView tvName, tvCategory, tvPrice, tvQuantity, tvStockValue,
            tvWarning, tvDescription, tvStatus;
    private Button btnImport, btnSell, btnBack, btnDelete;
    private DatabaseHelper dbHelper;
    private Product product;
    private int productId;
    private final NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvName = findViewById(R.id.tvDetailName);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvQuantity = findViewById(R.id.tvDetailQuantity);
        tvStockValue = findViewById(R.id.tvDetailStockValue);
        tvWarning = findViewById(R.id.tvDetailWarning);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvStatus = findViewById(R.id.tvDetailStatus);
        btnImport = findViewById(R.id.btnImport);
        btnSell = findViewById(R.id.btnSell);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new DatabaseHelper(this);
        productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            // không có id thì thoát luôn
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // nút nhập thêm: cộng 1
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product == null) return;
                int newQty = product.getQuantity() + 1;
                dbHelper.updateQuantity(productId, newQty);
                reload();
            }
        });

        // nút bán: trừ 1, hết hàng thì không cho bán nữa
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product == null) return;
                if (product.getQuantity() <= 0) {
                    Toast.makeText(ProductDetailActivity.this,
                            "Hết hàng", Toast.LENGTH_SHORT).show();
                    return;
                }
                int newQty = product.getQuantity() - 1;
                dbHelper.updateQuantity(productId, newQty);
                reload();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteProduct(productId);
                Toast.makeText(ProductDetailActivity.this,
                        "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        reload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    // tải lại thông tin sản phẩm từ database
    private void reload() {
        product = dbHelper.getProductById(productId);
        if (product == null) {
            Toast.makeText(this, "Sản phẩm đã bị xóa", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        tvName.setText(product.getName());
        tvCategory.setText("Loại: " + product.getCategory());
        tvPrice.setText("Giá bán: " + fmt.format(product.getPrice()) + " đ");
        tvQuantity.setText("Số lượng tồn: " + product.getQuantity());
        tvStockValue.setText("Giá trị tồn kho: "
                + fmt.format(product.getStockValue()) + " đ");
        String desc = product.getDescription();
        tvDescription.setText(desc == null || desc.isEmpty()
                ? "(Không có mô tả)" : desc);
        tvStatus.setText(product.isActive() ? "Trạng thái: Đang kinh doanh"
                : "Trạng thái: Ngừng kinh doanh");

        // tồn ít thì hiện cảnh báo đỏ
        if (product.isLowStock()) {
            tvWarning.setVisibility(View.VISIBLE);
            tvWarning.setText(product.getQuantity() == 0
                    ? "Hết hàng!" : "Cảnh báo: Sắp hết hàng!");
            tvQuantity.setTextColor(Color.RED);
        } else {
            tvWarning.setVisibility(View.GONE);
            tvQuantity.setTextColor(Color.BLACK);
        }
    }
}
