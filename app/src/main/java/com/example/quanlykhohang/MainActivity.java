package com.example.quanlykhohang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView tvStats;
    private ListView lvProducts;
    private Button btnAdd;
    private DatabaseHelper dbHelper;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStats = findViewById(R.id.tvStats);
        lvProducts = findViewById(R.id.lvProducts);
        btnAdd = findViewById(R.id.btnAdd);
        dbHelper = new DatabaseHelper(this);

        // bấm nút thì qua màn hình thêm
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        // bấm vào 1 dòng thì mở chi tiết, truyền id qua
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product p = productList.get(position);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("product_id", p.getId());
                startActivity(intent);
            }
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // quay lại màn hình này thì tải lại cho mới
        loadData();
    }

    private void loadData() {
        productList = dbHelper.getAllProducts();
        if (adapter == null) {
            adapter = new ProductAdapter(this, productList);
            lvProducts.setAdapter(adapter);
        } else {
            adapter.setData(productList);
        }

        // tính mấy số thống kê ở trên
        int total = productList.size();
        long totalValue = dbHelper.getTotalValue();
        int lowStock = dbHelper.countLowStock();

        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
        String stats = "Tổng số sản phẩm: " + total
                + "\nTổng giá trị kho: " + fmt.format(totalValue) + " đ"
                + "\nSắp hết hàng: " + lowStock;
        tvStats.setText(stats);

        TextView tvEmpty = findViewById(R.id.tvEmpty);
        if (tvEmpty != null) {
            tvEmpty.setVisibility(productList.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }
}
