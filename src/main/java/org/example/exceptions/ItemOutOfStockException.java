package org.example.exceptions;

public class ItemOutOfStockException extends RuntimeException {
    public ItemOutOfStockException(int itemId) {
        super(String.format("Item with id %d is out of stock", itemId));
    }
}
