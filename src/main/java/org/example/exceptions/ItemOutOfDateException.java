package org.example.exceptions;

public class ItemOutOfDateException extends RuntimeException  {

    public ItemOutOfDateException(String itemName) {
        super(String.format("%s can not be sold because it is expired", itemName));
    }

}
