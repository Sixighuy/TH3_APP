package com.example.quanlykhohang;

// class này chỉ để chứa thông tin 1 sản phẩm thôi
public class Product {
    private int id;
    private String name;
    private int price;      // giá bán, tính theo VND
    private int quantity;   // số lượng còn trong kho
    private String category;
    private int active;     // 1 là còn bán, 0 là ngừng bán
    private String description;

    public Product() {
    }

    public Product(int id, String name, int price, int quantity,
                   String category, int active, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.active = active;
        this.description = description;
    }

    public Product(String name, int price, int quantity,
                   String category, int active, String description) {
        this(-1, name, price, quantity, category, active, description);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getActive() { return active; }
    public void setActive(int active) { this.active = active; }

    public boolean isActive() { return active == 1; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // tiền tồn = giá * số lượng
    public long getStockValue() {
        return (long) price * quantity;
    }

    // dưới 5 cái thì coi như sắp hết
    public boolean isLowStock() {
        return quantity < 5;
    }
}
