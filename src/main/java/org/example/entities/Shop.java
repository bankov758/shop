package org.example.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop {

    private int id;

    private String name;

    private List<Cashier> cashiers = new ArrayList<Cashier>();

    private Map<Integer, ItemQuantity> deliveredItems = new HashMap<>();

    private Map<Integer, ItemQuantity> soldItems = new HashMap<>();

    private List<Receipt> receipts = new ArrayList<>();

    private double foodMarkupPercent;

    private double foodDiscountPercent;

    private double nonFoodMarkupPercent;

    private double nonFoodDiscountPercent;

    public Shop() {
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

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public void setCashiers(List<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    public Map<Integer, ItemQuantity> getDeliveredItems() {
        return deliveredItems;
    }

    public void setDeliveredItems(Map<Integer, ItemQuantity> deliveredItems) {
        this.deliveredItems = deliveredItems;
    }

    public Map<Integer, ItemQuantity> getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(Map<Integer, ItemQuantity> soldItems) {
        this.soldItems = soldItems;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public double getFoodMarkupPercent() {
        return foodMarkupPercent / 100;
    }

    public void setFoodMarkupPercent(double foodMarkupPercent) {
        this.foodMarkupPercent = foodMarkupPercent;
    }

    public double getFoodDiscountPercent() {
        return foodDiscountPercent / 100;
    }

    public void setFoodDiscountPercent(double foodDiscountPercent) {
        this.foodDiscountPercent = foodDiscountPercent;
    }

    public double getNonFoodMarkupPercent() {
        return nonFoodMarkupPercent / 100;
    }

    public void setNonFoodMarkupPercent(double nonFoodMarkupPercent) {
        this.nonFoodMarkupPercent = nonFoodMarkupPercent;
    }

    public double getNonFoodDiscountPercent() {
        return nonFoodDiscountPercent / 100;
    }

    public void setNonFoodDiscountPercent(double nonFoodDiscountPercent) {
        this.nonFoodDiscountPercent = nonFoodDiscountPercent;
    }

    public void addDeliveredItem(ItemQuantity itemQuantity) {
        Item itemToAdd = itemQuantity.getItem();
        deliveredItems.computeIfAbsent(itemToAdd.getId(), itemQty -> new ItemQuantity(itemToAdd, 0));
        deliveredItems.get(itemToAdd.getId()).addQuantity(itemQuantity.getQuantity());
    }

    public void sellItem(int itemId, double quantity) {
        ItemQuantity availableQuantity = deliveredItems.get(itemId);
        if (availableQuantity == null || availableQuantity.getQuantity() < quantity) {
            throw new IllegalArgumentException("Item with id" + itemId + " is out of stock");
        } else {
            deliveredItems.get(itemId).reduceQuantity(quantity);
        }
    }

    public void addSoldItem(ItemQuantity itemQuantity) {
        Item itemToAdd = itemQuantity.getItem();
        soldItems.computeIfAbsent(itemToAdd.getId(), itemQty -> new ItemQuantity(itemToAdd, 0));
        soldItems.get(itemToAdd.getId()).addQuantity(itemQuantity.getQuantity());
    }

    public double getCashierSalaries() {
        return cashiers
                .stream()
                .mapToDouble(Cashier::getSalary)
                .sum();
    }

    public double getDeliveredItemsExpense() {
        return deliveredItems
                .values()
                .stream()
                .mapToDouble(itemQty -> itemQty.getItem().getDeliveryPrice() * itemQty.getQuantity())
                .sum();
    }

    public double getSoldItemsIncome() {
        return receipts
                .stream()
                .mapToDouble(Receipt::getTotalCost)
                .sum();
    }

    public double getShopMonthlyIncome() {
        return getSoldItemsIncome() - getDeliveredItemsExpense() - getSoldItemsIncome();
    }

    public void addReceipts(Receipt receipt){
        receipts.add(receipt);
    }

}
