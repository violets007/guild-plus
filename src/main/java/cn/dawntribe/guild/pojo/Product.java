package cn.dawntribe.guild.pojo;

import cn.nukkit.item.Item;

/**
 * @author violets007
 * @version 1.0
 * @description: 商品
 * @date 2022/4/9 10:05 PM
 */
public class Product {
    private String owner;
    private Item item;
    private double price;
    private String createTime;

    public Product(String owner, Item item, double price, String createTime) {
        this.owner = owner;
        this.item = item;
        this.price = price;
        this.createTime = createTime;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return item != null ? item.equals(product.item) : product.item == null;
    }

    @Override
    public int hashCode() {
        return item != null ? item.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Product{" +
                "owner='" + owner + '\'' +
                ", item=" + item +
                ", price=" + price +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
