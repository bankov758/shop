import entities.Item;
import entities.ItemQuantity;
import entities.Shop;
import services.MockDataGenerator;
import services.SellingEngine;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Shop lidl = MockDataGenerator.generateMockShop();
        SellingEngine kasa1 = new SellingEngine(lidl, lidl.getCashiers().get(0));
        List<ItemQuantity> itemQuantities = MockDataGenerator.generateMockDeliveredItems();
        kasa1.processDelivery(itemQuantities);

        Item milk = itemQuantities.get(0).getItem();
        kasa1.sell(new ItemQuantity(milk, 5));

        System.out.printf(String.valueOf(kasa1.pay()));

        System.out.printf("");

    }
}
