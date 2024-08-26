package services;

import entities.*;

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

//        // Generating mock sold items
//        Map<Integer, ItemQuantity> soldItems = new HashMap<>();
//        soldItems.put(1, new ItemQuantity(new Item(1, "Milk", 2.0, 1.5), 80));
//        soldItems.put(2, new ItemQuantity(new Item(2, "Bread", 1.0, 0.7), 180));
//        shop.setSoldItems(soldItems);

//        // Generating mock receipts
//        List<Receipt> receipts = new ArrayList<>();
//        receipts.add(new Receipt(1, Arrays.asList(new ItemQuantity(new Item(1, "Milk", 2.0, 1.5), 2), new ItemQuantity(new Item(2, "Bread", 1.0, 0.7), 1)), 5.0));
//        receipts.add(new Receipt(2, Arrays.asList(new ItemQuantity(new Item(1, "Milk", 2.0, 1.5), 1), new ItemQuantity(new Item(2, "Bread", 1.0, 0.7), 3)), 6.0));
//        shop.setReceipts(receipts);

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
