package org.example.services;

import org.example.entities.*;

import java.time.LocalDateTime;
import java.util.List;

public class ShopService {

    private final Shop shop;

    private final Cashier cashier;

    private Receipt currentReceipt;

    private final ReceiptService receiptService;

    private static int receiptId = 1;

    public ShopService(Shop shop, Cashier cashier) {
        this.shop = shop;
        this.cashier = cashier;
        receiptService = new ReceiptService();
    }

    public void processDelivery(List<ItemQuantity> itemQuantities) {
        itemQuantities.forEach(this::addDeliveredItem);
    }

    public void sell(ItemQuantity itemQuantity) {
        if (currentReceipt == null) {
            currentReceipt = new Receipt(receiptId++, cashier, shop);
        }
        receiptService.addItems(itemQuantity, currentReceipt);
        sellItem(itemQuantity.getItem().getId(), itemQuantity.getQuantity());
    }

    public double pay() {
        addSoldItemsToShop(currentReceipt);
        currentReceipt.setPurchaseDate(LocalDateTime.now());
        double totalCost = receiptService.getTotalCost(currentReceipt);
        addReceipts(currentReceipt);
        currentReceipt = null;
        return totalCost;
    }

    public void addDeliveredItem(ItemQuantity itemQuantity) {
        Item itemToAdd = itemQuantity.getItem();
        shop.getDeliveredItems().computeIfAbsent(itemToAdd.getId(), itemQty -> new ItemQuantity(itemToAdd, 0));
        shop.getDeliveredItems().get(itemToAdd.getId()).addQuantity(itemQuantity.getQuantity());
    }

    public void sellItem(int itemId, double quantity) {
        ItemQuantity availableQuantity = shop.getDeliveredItems().get(itemId);
        if (availableQuantity == null || availableQuantity.getQuantity() < quantity) {
            throw new IllegalArgumentException("Item with id" + itemId + " is out of stock");
        } else {
            shop.getDeliveredItems().get(itemId).reduceQuantity(quantity);
        }
    }

    public void addSoldItem(ItemQuantity itemQuantity) {
        Item itemToAdd = itemQuantity.getItem();
        shop.getSoldItems().computeIfAbsent(itemToAdd.getId(), itemQty -> new ItemQuantity(itemToAdd, 0));
        shop.getSoldItems().get(itemToAdd.getId()).addQuantity(itemQuantity.getQuantity());
    }

    public double getCashierSalaries() {
        return shop.getCashiers()
                .stream()
                .mapToDouble(Cashier::getSalary)
                .sum();
    }

    public double getDeliveredItemsExpense() {
        return shop.getDeliveredItems()
                .values()
                .stream()
                .mapToDouble(itemQty -> itemQty.getItem().getDeliveryPrice() * itemQty.getQuantity())
                .sum();
    }

    public double getSoldItemsIncome() {
        return shop.getReceipts()
                .stream()
                .mapToDouble(receiptService::getTotalCost)
                .sum();
    }

    public double getShopMonthlyIncome() {
        return getSoldItemsIncome() - getDeliveredItemsExpense() - getSoldItemsIncome();
    }

    public void addReceipts(Receipt receipt) {
        shop.getReceipts().add(receipt);
    }

    public void addSoldItemsToShop(Receipt receipt) {
        for (ItemQuantity itemQuantity : receipt.getItemQuantities()) {
            addSoldItem(itemQuantity);
        }
    }

}
