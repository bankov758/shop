package org.example.services;

import org.example.entities.*;

import java.time.LocalDate;
import java.util.*;

public class MockDataGenerator {

    public static Shop generateMockShop() {
        Shop shop = new Shop();

        // Setting basic fields
        shop.setId(1);
        shop.setName("Lidl");

        // Setting markup and discount percentages
        shop.setFoodMarkupPercent(10.0);
        shop.setFoodDiscountPercent(5.0);
        shop.setNonFoodMarkupPercent(15.0);
        shop.setNonFoodDiscountPercent(7.5);

        // Generating mock cashiers
        List<Cashier> cashiers = new ArrayList<>();
        cashiers.add(new Cashier(1, "John Doe", 3000));
        cashiers.add(new Cashier(2, "Jane Smith", 3200));
        shop.setCashiers(cashiers);

        return shop;
    }

    public static List<ItemQuantity> generateMockDeliveredItems() {
        List<ItemQuantity> deliveredItems = new ArrayList<>();
        deliveredItems.add(new ItemQuantity(
                new Item(1, "Milk", 5.0,
                        ItemCategory.FOOD,
                        LocalDateToDate.convert(LocalDate.of(2024, 10, 15))),
                100));
        deliveredItems.add(new ItemQuantity(
                new Item(1, "Milk", 5.0,
                        ItemCategory.FOOD,
                        LocalDateToDate.convert(LocalDate.of(2024, 10, 15))),
                100));
        deliveredItems.add(new ItemQuantity(
                new Item(2, "Meds", 100.0,
                        ItemCategory.NON_FOOD,
                        LocalDateToDate.convert(LocalDate.of(2025, 1, 1))),
                20));
        return deliveredItems;
    }

}
