package org.example;

import org.example.entities.Item;

import org.example.entities.ItemQuantity;
import org.example.entities.Shop;
import org.example.helpers.MockDataGenerator;
import org.example.helpers.ReceiptPrinter;
import org.example.services.interfaces.ShopService;
import org.example.services.ShopServiceImpl;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Shop lidl = MockDataGenerator.generateMockShop();
        ShopService kasa1 = new ShopServiceImpl(lidl, lidl.getCashiers().get(0));
        List<ItemQuantity> itemQuantities = MockDataGenerator.generateMockDeliveredItems();
        kasa1.processDelivery(itemQuantities);

        Item milk = itemQuantities.get(0).getItem();
        kasa1.sell(new ItemQuantity(milk, 5));
        //kasa1.pay();
        Item meds = itemQuantities.get(2).getItem();
        kasa1.sell(new ItemQuantity(meds, 3));
        kasa1.pay();

        ReceiptPrinter.printReceipts(lidl.getReceipts());
    }

}