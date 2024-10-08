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
            //Normal case 1
            kasa1.sell(new ItemQuantity(itemQuantities.get("Milk").getItem(), 5));
            kasa1.sell(new ItemQuantity(itemQuantities.get("Meds").getItem(), 3));
            kasa1.sell(new ItemQuantity(itemQuantities.get("Pork").getItem(), 3));
            ReceiptPrinter.printReceipt(kasa1.pay());

            //Normal case 2
            kasa1.sell(new ItemQuantity(itemQuantities.get("Toilet Paper").getItem(), 1));
            kasa1.sell(new ItemQuantity(itemQuantities.get("Soap").getItem(), 1));
            ReceiptPrinter.printReceipt(kasa1.pay());

            //Expense information
            System.out.printf("Expense for goods %.2f\n", kasa1.getDeliveredItemsExpense());
            System.out.printf("Income from goods %.2f\n", kasa1.getSoldItemsIncome());
            System.out.printf("Expense for cashier salaries %.2f\n", kasa1.getCashierSalaries());
            System.out.printf("Overall income %.2f\n", kasa1.getShopMonthlyIncome());

            //Out of stock
            kasa1.sell(new ItemQuantity(itemQuantities.get("Milk").getItem(), 5));
            ReceiptPrinter.printReceipt(kasa1.pay());

            //Out of date
            kasa1.sell(new ItemQuantity(itemQuantities.get("Chicken").getItem(), 5));
            ReceiptPrinter.printReceipt(kasa1.pay());

        } catch (ItemOutOfStockException e) {
            Logger logger = Logger.getLogger(Main.class.getName());
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

}