package entities;

import java.util.List;

public class Shop {

    private int id;

    private String name;

    private List<Cashier> cashiers;

    private List<ItemQuantity> deliveredItems;

    private List<ItemQuantity> soldItems;

    private List<Receipt> receipts;

    //todo
    /*
     * Хранителните и нехранителните стоки имат различен % надценка, който се определя в магазина.
     * Ако срокът на годност наближава, т.е. остават по-малко от даден брой дни до изтичането му,
     * продажната цена на стоката се намалява с определен %. Броят на дните до изтичането на срока
     * и % намаление са различни за всеки магазин. Стоки с изтекъл срок на годност не трябва да се продават
     * */

    private double foodMarkupPercent;

    private double foodDiscountPercent;

    private double nonFoodMarkupPercent;

    private double nonFoodDiscountPercent;

    public Shop() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public void setCashiers(List<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    public List<ItemQuantity> getDeliveredItems() {
        return deliveredItems;
    }

    public void setDeliveredItems(List<ItemQuantity> deliveredItems) {
        this.deliveredItems = deliveredItems;
    }

    public List<ItemQuantity> getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(List<ItemQuantity> soldItems) {
        this.soldItems = soldItems;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public double getCashierSalaries() {
        return cashiers
                .stream()
                .mapToDouble(Cashier::getSalary)
                .sum();
    }

    public double getDeliveredItemsExpense() {
        return deliveredItems
                .stream()
                .mapToDouble(itemQty -> itemQty.getItem().getDeliveryPrice() * itemQty.getQuantity())
                .sum();
    }
}
