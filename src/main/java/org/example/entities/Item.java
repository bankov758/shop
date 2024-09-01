package org.example.entities;

import java.util.Date;

public class Item {

    private int id;

    private String name;

    private double deliveryPrice;

    private ItemCategory category;

    private Date expirationDate;

    public Item() {
    }

    public Item(int id, String name, double deliveryPrice, ItemCategory category, Date expirationDate) {
        this.id = id;
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.category = category;
        this.expirationDate = expirationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
