package org.example.helpers;

import org.example.entities.*;

import java.time.LocalDate;
import java.util.*;

public class MockDataGenerator {

    private static int itemId = 0;

    private static int cashierId = 0;

    private static int shopId = 0;

    public static Shop generateMockShop() {
        Shop shop = new Shop();

        // Setting basic fields
        shop.setId(shopId++);
        shop.setName("Lidl");

        // Setting markup and discount percentages
        shop.setFoodMarkupPercent(10.0);
        shop.setFoodDiscountPercent(0.1);
        shop.setNonFoodMarkupPercent(15.0);
        shop.setNonFoodDiscountPercent(0.01);

        // Generating mock cashiers
        List<Cashier> cashiers = new ArrayList<>();
        cashiers.add(new Cashier(cashierId++, "John Doe", 3000));
        cashiers.add(new Cashier(cashierId++, "Jane Smith", 3200));
        shop.setCashiers(cashiers);

        return shop;
    }

    public static Map<String, ItemQuantity> generateMockDeliveredItems() {
        Map<String, ItemQuantity> deliveredItems = new HashMap<>();
        deliveredItems.put("Milk", new ItemQuantity(
                new Item(itemId++, "Milk", 3.0,
                        ItemCategory.FOOD,
                        DateHelper.convertLocalDateToDate(LocalDate.of(2024, 10, 15))),
                8));
        deliveredItems.put("Chicken", new ItemQuantity(
                new Item(itemId++, "Chicken", 5.0,
                        ItemCategory.FOOD,
                        DateHelper.convertLocalDateToDate(LocalDate.of(2024, 9, 15))),
                250));
        deliveredItems.put("Pork", new ItemQuantity(
                new Item(itemId++, "Pork", 7.0,
                        ItemCategory.FOOD,
                        DateHelper.convertLocalDateToDate(LocalDate.of(2024, 10, 15))),
                250));
        deliveredItems.put("Cheese",new ItemQuantity(
                new Item(itemId++, "Cheese", 15.0,
                        ItemCategory.FOOD,
                        DateHelper.convertLocalDateToDate(LocalDate.of(2024, 10, 15))),
                250));
        deliveredItems.put("Chocolate",new ItemQuantity(
                new Item(itemId++, "Chocolate", 4.0,
                        ItemCategory.FOOD,
                        DateHelper.convertLocalDateToDate(LocalDate.of(2024, 10, 15))),
                250));
        deliveredItems.put("Meds",new ItemQuantity(
                new Item(itemId++, "Meds", 10.0,
                        ItemCategory.NON_FOOD,
                        DateHelper.convertLocalDateToDate(LocalDate.of(2025, 1, 1))),
                20));
        deliveredItems.put("Toilet Paper",new ItemQuantity(
                new Item(itemId++, "Toilet Paper", 4.5,
                        ItemCategory.NON_FOOD,
                        DateHelper.convertLocalDateToDate(LocalDate.of(2025, 1, 1))),
                150));
        deliveredItems.put("Soap", new ItemQuantity(
                new Item(itemId++, "Soap", 0.9,
                        ItemCategory.NON_FOOD,
                        DateHelper.convertLocalDateToDate(LocalDate.of(2025, 1, 1))),
                300));
        return deliveredItems;
    }

}
