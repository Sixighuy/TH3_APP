package com.example.quanlykhohang;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

// adapter để đổ sản phẩm lên ListView ở màn hình chính
public class ProductAdapter extends BaseAdapter {

    private final Context context;
    private List<Product> data;
    private final NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));

    public ProductAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<Product> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_product, parent, false);
        }
        Product p = data.get(position);

        TextView tvName = convertView.findViewById(R.id.tvItemName);
        TextView tvPrice = convertView.findViewById(R.id.tvItemPrice);
        TextView tvCateQty = convertView.findViewById(R.id.tvItemCateQty);
        TextView tvWarning = convertView.findViewById(R.id.tvItemWarning);

        tvName.setText(p.getName());
        tvPrice.setText(fmt.format(p.getPrice()) + " đ");
        tvCateQty.setText(p.getCategory() + " - Tồn: " + p.getQuantity());

        // hàng sắp hết thì hiện chữ đỏ cho dễ thấy
        if (p.isLowStock()) {
            tvWarning.setVisibility(View.VISIBLE);
            tvWarning.setText("Sắp hết hàng");
            tvCateQty.setTextColor(Color.RED);
        } else {
            tvWarning.setVisibility(View.GONE);
            tvCateQty.setTextColor(Color.DKGRAY);
        }
        return convertView;
    }
}
