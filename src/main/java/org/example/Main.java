package org.example;

import org.example.entities.Item;

import org.example.entities.ItemQuantity;
import org.example.entities.Shop;
import org.example.exceptions.ItemOutOfStockException;
import org.example.helpers.MockDataGenerator;
import org.example.helpers.ReceiptPrinter;
import org.example.services.interfaces.ShopService;
import org.example.services.ShopServiceImpl;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        Shop lidl = MockDataGenerator.generateMockShop();
        ShopService kasa1 = new ShopServiceImpl(lidl, lidl.getCashiers().get(0));
        Map<String, ItemQuantity> itemQuantities = MockDataGenerator.generateMockDeliveredItems();
        kasa1.processDelivery(itemQuantities.values().stream().toList());

        try {
            //Normal case
            kasa1.sell(new ItemQuantity(itemQuantities.get("Milk").getItem(), 5));
            kasa1.sell(new ItemQuantity(itemQuantities.get("Meds").getItem(), 3));
            ReceiptPrinter.printReceipt(kasa1.pay());

            //Out of stock
            kasa1.sell(new ItemQuantity(itemQuantities.get("Milk").getItem(), 5));
            kasa1.sell(new ItemQuantity(itemQuantities.get("Meds").getItem(), 3));
            ReceiptPrinter.printReceipt(kasa1.pay());
        } catch (ItemOutOfStockException e) {
            Logger logger = Logger.getLogger(Main.class.getName());
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

}