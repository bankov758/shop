package org.example.services.interfaces;

import org.example.entities.ItemQuantity;
import org.example.entities.Receipt;
import org.example.entities.Shop;

public interface ReceiptService {

    /**
     * Calculates the total cost of items based on their delivery price, markup, and discount.
     * This calculation considers the number of days between the item's expiration date and the purchase date.
     * The total cost adjusts for each item's category (food or non-food), applying the relevant markup percentage
     * and discount based on the days until expiration. Markup and discounts are defined separately for food
     * and non-food categories in the {@link Shop} class.
     *
     * @return the calculated total cost of all items, factoring in delivery price, markup,
     * and discounts affected by the number of days between the expiration date and the purchase date.
     */
    double getTotalCost(Receipt receipt);

    double getItemPrice(ItemQuantity itemQuantity, Receipt receipt);

    void addItems(ItemQuantity itemQuantity, Receipt receipt);

}
