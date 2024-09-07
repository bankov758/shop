package org.example.services;

import org.example.entities.*;
import org.example.services.interfaces.ReceiptService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class ReceiptServiceImpl implements ReceiptService {

    public ReceiptServiceImpl() {}

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

    @Override
    public void addItems(ItemQuantity itemQuantity, Receipt receipt){
        receipt.getItemQuantities().add(itemQuantity);
    }

}
