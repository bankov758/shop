package services;

import entities.Cashier;
import entities.ItemQuantity;
import entities.Receipt;
import entities.Shop;

import java.time.LocalDateTime;
import java.util.List;

public class SellingEngine {

    private final Shop shop;

    private final Cashier cashier;

    private Receipt currentReceipt;

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
            currentReceipt = new Receipt(1, cashier, shop);
        }
        currentReceipt.addItems(itemQuantity);
        shop.sellItem(itemQuantity.getItem().getId(), itemQuantity.getQuantity());
    }

    public double pay() {
        currentReceipt.addSoldItemsTosShop();
        currentReceipt.setPurchaseDate(LocalDateTime.now());
        double totalCost = currentReceipt.getTotalCost();
        shop.addReceipts(currentReceipt);
        currentReceipt = null;
        return totalCost;
    }

}
