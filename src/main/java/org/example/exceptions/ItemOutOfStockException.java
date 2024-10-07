package org.example.exceptions;

public class ItemOutOfStockException extends RuntimeException {
    public ItemOutOfStockException(String itemName, double insufficientAmount) {
        super(String.format("%s is out of stock, %.2f more needed to complete the purchase",
                itemName, insufficientAmount));
    }
}
