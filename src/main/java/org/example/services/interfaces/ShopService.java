package org.example.services.interfaces;

import org.example.entities.ItemQuantity;
import org.example.entities.Receipt;

import java.util.List;

public interface ShopService {

    Receipt getCurrentReceipt();

    void processDelivery(List<ItemQuantity> itemQuantities);

    void sell(ItemQuantity itemQuantity);

    void pay();

    void addDeliveredItem(ItemQuantity itemQuantity);

    void sellItem(ItemQuantity itemQuantity);

    void addSoldItem(ItemQuantity itemQuantity);

    double getCashierSalaries();

    double getDeliveredItemsExpense();

    double getSoldItemsIncome();

    double getShopMonthlyIncome();

    void addReceiptToShop(Receipt receipt);

    void addSoldItemsToShop(Receipt receipt);

}
