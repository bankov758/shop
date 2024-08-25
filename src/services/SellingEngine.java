package services;

import entities.ItemQuantity;
import entities.Shop;

import java.util.List;

public class SellingEngine {

    private Shop shop;

    public SellingEngine(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void processDelivery(List<ItemQuantity> itemQuantities){
        itemQuantities.forEach(itemQuantity -> {shop.addDeliveredItem(itemQuantity);});
    }

    public void sell() {}

}
