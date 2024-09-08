package org.example.services;

import org.example.entities.*;
import org.example.exceptions.ItemOutOfDateException;
import org.example.services.interfaces.ReceiptService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class ReceiptServiceImpl implements ReceiptService {

    public ReceiptServiceImpl() {
    }

    @Override
    public double getTotalCost(Receipt receipt) {
        double totalCost = 0;
        for (ItemQuantity itemQuantity : receipt.getItemQuantities()) {
            totalCost += getItemPrice(itemQuantity, receipt) * itemQuantity.getQuantity();
        }
        return totalCost;
    }

    @Override
    public double getItemPrice(ItemQuantity itemQuantity, Receipt receipt) {
        Item item = itemQuantity.getItem();
        LocalDate expirationDate = item.getExpirationDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        long daysBetween = -1 * ChronoUnit.DAYS.between(expirationDate, receipt.getPurchaseDate());
        if (daysBetween < 0) {
            throw new ItemOutOfDateException(item.getName());
        }
        double markedUpPrice = item.getDeliveryPrice() + round(item.getDeliveryPrice() * getMarkup(receipt, item), 2);
        double priceDiscount = round(daysBetween * getDiscount(receipt, item) * item.getDeliveryPrice(), 2);
        return markedUpPrice - priceDiscount;
    }

    private static double getMarkup(Receipt receipt, Item item) {
        return item.getCategory() == ItemCategory.FOOD ?
                receipt.getShop().getFoodMarkupPercent() : receipt.getShop().getNonFoodMarkupPercent();
    }

    private static double getDiscount(Receipt receipt, Item item) {
        return item.getCategory() == ItemCategory.FOOD ?
                receipt.getShop().getFoodDiscountPercent() : receipt.getShop().getNonFoodDiscountPercent();
    }

    public static double round(double value, int precision) {
        return Math.round(value * Math.pow(10.0, precision)) / Math.pow(10.0, precision);
    }

    @Override
    public void addItems(ItemQuantity itemQuantity, Receipt receipt) {
        receipt.getItemQuantities().add(itemQuantity);
    }

}
