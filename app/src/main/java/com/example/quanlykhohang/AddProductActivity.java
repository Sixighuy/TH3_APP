package com.example.quanlykhohang;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class AddProductActivity extends Activity {

    private EditText edtName, edtPrice, edtQuantity, edtDescription;
    private Spinner spnCategory;
    private Switch switchActive;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    // mấy loại để chọn nhanh trong spinner
    private static final String[] CATEGORIES = {
            "Điện tử", "Thực phẩm", "Văn phòng phẩm", "Gia dụng", "Khác"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtDescription = findViewById(R.id.edtDescription);
        spnCategory = findViewById(R.id.spnCategory);
        switchActive = findViewById(R.id.switchActive);
        btnSave = findViewById(R.id.btnSave);
        dbHelper = new DatabaseHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, CATEGORIES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
        switchActive.setChecked(true); // mặc định là đang bán

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    private void saveProduct() {
        String name = edtName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String qtyStr = edtQuantity.getText().toString().trim();
        String category = spnCategory.getSelectedItem().toString();
        int active = switchActive.isChecked() ? 1 : 0;
        String description = edtDescription.getText().toString().trim();

        // kiểm tra từng ô, sai thì báo ngay tại ô đó
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Tên sản phẩm không được để trống");
            return;
        }
        if (TextUtils.isEmpty(priceStr)) {
            edtPrice.setError("Giá bán không được để trống");
            return;
        }
        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            edtPrice.setError("Giá bán không hợp lệ");
            return;
        }
        if (price <= 0) {
            edtPrice.setError("Giá bán phải lớn hơn 0");
            return;
        }
        if (TextUtils.isEmpty(qtyStr)) {
            edtQuantity.setError("Số lượng không được để trống");
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            edtQuantity.setError("Số lượng không hợp lệ");
            return;
        }
        if (quantity < 0) {
            edtQuantity.setError("Số lượng không được âm");
            return;
        }

        Product p = new Product(name, price, quantity, category, active, description);
        long id = dbHelper.insertProduct(p);
        if (id > 0) {
            Toast.makeText(this, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lưu thất bại, thử lại", Toast.LENGTH_SHORT).show();
        }
    }
}
