package org.example.services;

import org.example.entities.*;
import org.example.exceptions.ItemOutOfStockException;

import java.time.LocalDateTime;
import java.util.List;

public class ShopService {

    private final Shop shop;

    private final Cashier cashier;

    private Receipt currentReceipt;

    private final ReceiptService receiptService;

    private static int receiptId = 1;

    public ShopService(Shop shop, Cashier cashier, ReceiptService receiptService) {
        this.shop = shop;
        this.cashier = cashier;
        this.receiptService = receiptService;
    }

    public ShopService(Shop shop, Cashier cashier) {
        this.shop = shop;
        this.cashier = cashier;
        this.receiptService = new ReceiptService();
    }

    public Receipt getCurrentReceipt() {
        return currentReceipt;
    }

    public void processDelivery(List<ItemQuantity> itemQuantities) {
        itemQuantities.forEach(this::addDeliveredItem);
    }

    public void sell(ItemQuantity itemQuantity) {
        if (getCurrentReceipt() == null) {
            currentReceipt = new Receipt(receiptId++, cashier, shop);
        }
        receiptService.addItems(itemQuantity, getCurrentReceipt());
        sellItem(itemQuantity.getItem().getId(), itemQuantity.getQuantity());
    }

    public void pay() {
        addSoldItemsToShop(getCurrentReceipt());
        getCurrentReceipt().setPurchaseDate(LocalDateTime.now());
        addReceiptToShop(getCurrentReceipt());
        currentReceipt = null;
    }

    public void addDeliveredItem(ItemQuantity itemQuantity) {
        Item itemToAdd = itemQuantity.getItem();
        shop.getDeliveredItems().computeIfAbsent(itemToAdd.getId(), itemQty -> new ItemQuantity(itemToAdd, 0));
        shop.getDeliveredItems().get(itemToAdd.getId()).addQuantity(itemQuantity.getQuantity());
    }

    public void sellItem(int itemId, double quantity) {
        ItemQuantity availableQuantity = shop.getDeliveredItems().get(itemId);
        if (availableQuantity == null || availableQuantity.getQuantity() < quantity) {
            throw new ItemOutOfStockException(itemId);
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
        return getSoldItemsIncome() - getDeliveredItemsExpense() - getCashierSalaries();
    }

    public void addReceiptToShop(Receipt receipt) {
        shop.getReceipts().add(receipt);
    }

    public void addSoldItemsToShop(Receipt receipt) {
        for (ItemQuantity itemQuantity : receipt.getItemQuantities()) {
            addSoldItem(itemQuantity);
        }
    }

}
