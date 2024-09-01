package org.example.services;

import org.example.entities.*;

import java.time.LocalDateTime;
import java.util.List;

public class SellingEngine {

    private final Shop shop;

    private final Cashier cashier;

    private Receipt currentReceipt;

    private static int receiptId = 1;

    public SellingEngine(Shop shop, Cashier cashier) {
        this.shop = shop;
        this.cashier = cashier;
    }

    public Shop getShop() {
        return shop;
    }

    public void processDelivery(List<ItemQuantity> itemQuantities){
        itemQuantities.forEach(shop::addDeliveredItem);
    }

    public void sell(ItemQuantity itemQuantity) {
        if (currentReceipt == null) {
            currentReceipt = new Receipt(receiptId++, cashier, shop);
        }
        currentReceipt.addItems(itemQuantity);
        shop.sellItem(itemQuantity.getItem().getId(), itemQuantity.getQuantity());
    }

    public double pay() {
        currentReceipt.addSoldItemsToShop();
        currentReceipt.setPurchaseDate(LocalDateTime.now());
        double totalCost = currentReceipt.getTotalCost();
        shop.addReceipts(currentReceipt);
        currentReceipt = null;
        return totalCost;
    }

}
