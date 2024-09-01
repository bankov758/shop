package org.example.entities;

public class ItemQuantity {

    private Item item;

    private double quantity;

    public ItemQuantity(Item item, double quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public double getQuantity() {
        return quantity;
    }

    public void addQuantity(double quantity) {
        this.quantity += quantity;
    }

    public void reduceQuantity(double quantity) {
        this.quantity -= quantity;
    }
}
