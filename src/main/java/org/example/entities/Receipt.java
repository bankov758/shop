package org.example.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Receipt {

    private int id;

    private Cashier cashier;

    private LocalDateTime purchaseDate;

    private List<ItemQuantity> itemQuantities = new ArrayList<>();

    private Shop shop;

    public Receipt() {
    }

    public Receipt(int id, Cashier cashier, Shop shop) {
        this.id = id;
        this.cashier = cashier;
        this.shop = shop;
    }

    public Receipt(int id, Cashier cashier, LocalDateTime purchaseDate,
                   List<ItemQuantity> itemQuantities, Shop shop) {
        this.id = id;
        this.cashier = cashier;
        this.purchaseDate = purchaseDate;
        this.itemQuantities = itemQuantities;
        this.shop = shop;
    }

    public List<ItemQuantity> getItemQuantities() {
        return itemQuantities;
    }

    public void setItemQuantities(List<ItemQuantity> itemQuantities) {
        this.itemQuantities = itemQuantities;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    /**
     * Calculates the total cost of items based on their delivery price, markup, and discount.
     * This calculation considers the number of days between the item's expiration date and the purchase date.
     * The total cost adjusts for each item's category (food or non-food), applying the relevant markup percentage
     * and discount based on the days until expiration. Markup and discounts are defined separately for food
     * and non-food categories in the {@link Shop} class.
     *
     * @return the calculated total cost of all items, factoring in delivery price, markup,
     * and discounts affected by the number of days between the expiration date and the purchase date.
     */
    public double getTotalCost() {
        double totalCost = 0;
        for (ItemQuantity itemQuantity : itemQuantities) {
            totalCost += getItemPrice(itemQuantity) * itemQuantity.getQuantity();
        }
        return totalCost;
    }

    public double getItemPrice(ItemQuantity itemQuantity) {
        Item item = itemQuantity.getItem();
        double markup = item.getCategory() == ItemCategory.FOOD ?
                shop.getFoodMarkupPercent() : shop.getNonFoodMarkupPercent();
        double discount = item.getCategory() == ItemCategory.FOOD ?
                shop.getFoodDiscountPercent() : shop.getNonFoodDiscountPercent();
        LocalDate expirationDate = item.getExpirationDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(expirationDate, purchaseDate);
        return item.getDeliveryPrice() + item.getDeliveryPrice() * markup - daysBetween * discount;
    }

    public void addSoldItemsToShop() {
        for (ItemQuantity itemQuantity : itemQuantities) {
            shop.addSoldItem(itemQuantity);
        }
    }

    public void addItems(ItemQuantity itemQuantity){
        itemQuantities.add(itemQuantity);
    }

}
