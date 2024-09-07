package org.example.services;

import org.example.entities.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class ReceiptService {

    public ReceiptService() {}

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
    public double getTotalCost(Receipt receipt) {
        double totalCost = 0;
        for (ItemQuantity itemQuantity : receipt.getItemQuantities()) {
            totalCost += getItemPrice(itemQuantity, receipt) * itemQuantity.getQuantity();
        }
        return totalCost;
    }

    public double getItemPrice(ItemQuantity itemQuantity, Receipt receipt) {
        Item item = itemQuantity.getItem();
        double markup = item.getCategory() == ItemCategory.FOOD ?
                receipt.getShop().getFoodMarkupPercent() : receipt.getShop().getNonFoodMarkupPercent();
        double discount = item.getCategory() == ItemCategory.FOOD ?
                receipt.getShop().getFoodDiscountPercent() : receipt.getShop().getNonFoodDiscountPercent();
        LocalDate expirationDate = item.getExpirationDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(expirationDate, receipt.getPurchaseDate());
        return item.getDeliveryPrice() + item.getDeliveryPrice() * markup - daysBetween * discount;
    }

    public void addItems(ItemQuantity itemQuantity, Receipt receipt){
        receipt.getItemQuantities().add(itemQuantity);
    }

}
