package entities;

import java.time.LocalDateTime;
import java.util.List;

public class Receipt {

    private int id;

    private Cashier cashier;

    private LocalDateTime purchaseDate;

    private List<ItemQuantity> itemQuantities;

    private Shop shop;

    public Receipt() {
    }

    public Receipt(int id, Cashier cashier, LocalDateTime purchaseDate, List<ItemQuantity> itemQuantities, Shop shop) {
        this.id = id;
        this.cashier = cashier;
        this.purchaseDate = purchaseDate;
        this.itemQuantities = itemQuantities;
        this.shop = shop;
    }

    public List<ItemQuantity> getItemQuantities() {
        return itemQuantities;
    }

    public void setItemQuantities(List<ItemQuantity> itemQuantities) {
        this.itemQuantities = itemQuantities;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public double getTotalCost(){
        //delcost + delCost*markup - (perChaseDate - delDate) * discount
        return itemQuantities.stream().mapToDouble()
    }
}
