import org.example.entities.*;
import org.example.exceptions.ItemOutOfStockException;
import org.example.services.interfaces.ReceiptService;
import org.example.services.ReceiptServiceImpl;
import org.example.services.interfaces.ShopService;
import org.example.services.ShopServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.time.LocalDate.of;
import static org.example.helpers.LocalDateToDate.convert;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShopServiceImplTests {

    private ShopService shopService;
    private Shop shop;

    @BeforeEach
    void setup() {
        shop = new Shop();
        shopService = spy(new ShopServiceImpl(shop, new Cashier()));
    }

    @Test
    public void whenMultipleItemQuantitiesProcessed_thenQuantitiesAreUpdatedCorrectly() {
        // Arrange
        List<ItemQuantity> itemQuantities = List.of(
                new ItemQuantity(new Item(1, "Item1", 10.0, ItemCategory.FOOD,
                        convert(of(2024, 10, 15))), 5),
                new ItemQuantity(new Item(2, "Item2", 15.0, ItemCategory.NON_FOOD,
                        convert(of(2024, 10, 15))), 3)
        );

        // Act
        shopService.processDelivery(itemQuantities);

        // Assert
        assertEquals(5, shop.getDeliveredItems().get(1).getQuantity());
        assertEquals(3, shop.getDeliveredItems().get(2).getQuantity());
    }

    @Test
    public void whenExistingItemQuantityUpdated_thenNewQuantityIsCorrect() {
        // Arrange
        Item item = new Item(1, "Item2", 15.0, ItemCategory.NON_FOOD,
                convert(of(2024, 10, 15)));
        shop.getDeliveredItems().put(1, new ItemQuantity(item, 5));
        List<ItemQuantity> itemQuantities = List.of(
                new ItemQuantity(item, 3)
        );

        // Act
        shopService.processDelivery(itemQuantities);

        // Assert
        assertEquals(8, shop.getDeliveredItems().get(1).getQuantity());
    }

    @Test
    public void whenSellItemAndCurrentReceiptInitialized_thenSameReceiptIsReturned() {
        // Arrange
        doNothing().when(shopService).sellItem(anyInt(), anyDouble());
        Item item = new Item(1, "Test Item", 10.0, ItemCategory.FOOD, new Date());
        ItemQuantity itemQuantity = new ItemQuantity(item, 1);

        // Act
        shopService.sell(itemQuantity);
        Receipt firstReceipt = shopService.getCurrentReceipt();
        shopService.sell(itemQuantity);

        // Assert
        assertEquals(firstReceipt, shopService.getCurrentReceipt());
    }

    @Test
    public void whenItemQuantityAddedToCurrentReceipt_thenReceiptContainsItemQuantity() {
        // Arrange
        Item item = new Item(1, "Test Item", 10.0, ItemCategory.FOOD, new Date());
        ItemQuantity itemQuantity = new ItemQuantity(item, 1);
        doNothing().when(shopService).sellItem(item.getId(), itemQuantity.getQuantity());

        // Act
        shopService.sell(itemQuantity);

        // Assert
        assertTrue(shopService.getCurrentReceipt().getItemQuantities().contains(itemQuantity));
    }

    @Test
    public void whenSellCalled_thenSellItemMethodIsInvoked() {
        // Arrange
        Item item = new Item(1, "Test Item", 10.0, ItemCategory.FOOD, new Date());
        ItemQuantity itemQuantity = new ItemQuantity(item, 1);
        doNothing().when(shopService).sellItem(item.getId(), itemQuantity.getQuantity());

        // Act
        shopService.sell(itemQuantity);

        // Assert
        verify(shopService, times(1))
                .sellItem(item.getId(), itemQuantity.getQuantity());
    }

    @Test
    public void whenSellItemMultipleTimes_thenQuantityIsReducedSuccessfully() {
        // Arrange
        Item item = new Item(1, "Test Item", 10.0, ItemCategory.FOOD, new Date());
        ItemQuantity itemQuantity = new ItemQuantity(item, 10);
        shop.getDeliveredItems().put(1, itemQuantity);

        // Act
        shopService.sellItem(1, 3);
        shopService.sellItem(1, 3);

        // Assert
        assertEquals(4, shop.getDeliveredItems().get(1).getQuantity());
    }

    @Test
    public void whenSellItemWithInvalidId_thenItemOutOfStockExceptionIsThrown() {
        // Act & Assert
        assertThrows(ItemOutOfStockException.class, () -> {
            shopService.sellItem(999, 5);
        });
    }

    @Test
    public void whenSellItemExceedingStock_thenItemOutOfStockExceptionIsThrown() {
        // Arrange
        Item item = new Item(1, "Test Item", 10.0, ItemCategory.FOOD, new Date());
        shop.getDeliveredItems().put(1, new ItemQuantity(item, 5));

        // Act & Assert
        assertThrows(ItemOutOfStockException.class, () -> {
            shopService.sellItem(1, 10);
        });
    }

    @Test
    public void whenNewItemIsSold_thenItIsAddedToSoldItems() {
        // Arrange
        Item item = new Item(1, "Apple", 0.5, ItemCategory.FOOD, new Date());
        ItemQuantity itemQuantity = new ItemQuantity(item, 10);

        // Act
        shopService.addSoldItem(itemQuantity);

        // Assert
        assertTrue(shop.getSoldItems().containsKey(item.getId()));
        assertEquals(10, shop.getSoldItems().get(item.getId()).getQuantity());
    }

    @Test
    public void whenExistingSoldItemQuantityIncreased_thenQuantityIsCorrect() {
        // Arrange
        Item item = new Item(1, "Apple", 0.5, ItemCategory.FOOD, new Date());
        ItemQuantity initialQuantity = new ItemQuantity(item, 5);
        shopService.addSoldItem(initialQuantity);
        ItemQuantity additionalQuantity = new ItemQuantity(item, 10);

        // Act
        shopService.addSoldItem(additionalQuantity);

        // Assert
        assertEquals(15, shop.getSoldItems().get(item.getId()).getQuantity());
    }

    @Test
    public void whenMultipleCashiersPresent_thenTotalSalariesCalculatedCorrectly() {
        // Arrange
        shop.setCashiers(List.of(
                new Cashier(1, "John", 1000.0),
                new Cashier(2, "Jane", 1500.0)
        ));

        // Act
        double totalSalaries = shopService.getCashierSalaries();

        // Assert
        assertEquals(2500.0, totalSalaries);
    }

    @Test
    public void whenNoCashiers_thenTotalSalariesIsZero() {
        // Arrange
        shop.setCashiers(new ArrayList<>());

        // Act
        double totalSalaries = shopService.getCashierSalaries();

        // Assert
        assertEquals(0.0, totalSalaries);
    }

    @Test
    public void whenMultipleItemsDelivered_thenTotalExpenseIsCorrect() {
        // Arrange
        Item item1 = new Item(1, "Item1", 10.0, null, null);
        Item item2 = new Item(2, "Item2", 20.0, null, null);
        shop.getDeliveredItems().put(1, new ItemQuantity(item1, 2));
        shop.getDeliveredItems().put(2, new ItemQuantity(item2, 3));

        // Act
        double expense = shopService.getDeliveredItemsExpense();

        // Assert
        assertEquals(80.0, expense);
    }

    @Test
    public void whenNoDeliveredItems_thenTotalExpenseIsZero() {
        // Act
        double expense = shopService.getDeliveredItemsExpense();

        // Assert
        assertEquals(0.0, expense);
    }

    @Test
    public void whenReceiptsPresent_thenTotalCostIsCorrectlySummed() {
        // Arrange
        Shop shop = new Shop();
        shop.setReceipts(Arrays.asList(new Receipt(), new Receipt(), new Receipt()));
        ReceiptService receiptService = spy(new ReceiptServiceImpl());
        doReturn(50.0, 75.0, 25.0)
                .when(receiptService)
                .getTotalCost(any(Receipt.class));
        ShopService service = new ShopServiceImpl(shop, new Cashier(), receiptService);

        // Act & Assert
        assertEquals(150.0, service.getSoldItemsIncome());
    }

    @Test
    public void whenCalculatingMonthlyIncome_thenCorrectIncomeIsReturned() {
        // Arrange
        doReturn(100000.0).when(shopService).getSoldItemsIncome();
        doReturn(30000.0).when(shopService).getDeliveredItemsExpense();
        doReturn(10000.0).when(shopService).getCashierSalaries();

        // Act & Assert
        assertEquals(60000.0, shopService.getShopMonthlyIncome());
    }

    @Test
    public void whenValidReceiptAdded_thenReceiptIsStoredInShop() {
        // Arrange
        Receipt receipt = new Receipt(1, new Cashier(), shop);

        // Act
        shopService.addReceiptToShop(receipt);

        // Assert
        assertEquals(1, shop.getReceipts().size());
        assertEquals(receipt, shop.getReceipts().get(0));
    }

    @Test
    public void whenReceiptAdded_thenSoldItemsAreUpdated() {
        // Arrange
        Cashier cashier = new Cashier();
        Item item1 = new Item(1, "Item1", 10.0, null, null);
        Item item2 = new Item(2, "Item2", 20.0, null, null);
        List<ItemQuantity> itemQuantities = List.of(new ItemQuantity(item1, 2), new ItemQuantity(item2, 3));
        Receipt receipt = new Receipt(1, cashier, LocalDateTime.now(), itemQuantities, shop);

        // Act
        shopService.addSoldItemsToShop(receipt);

        // Assert
        assertEquals(2, shop.getSoldItems().get(1).getQuantity());
        assertEquals(3, shop.getSoldItems().get(2).getQuantity());
    }

    @Test
    public void whenPay_thenPurchaseDateIsSetAndReceiptIsReset() {
        // Arrange
        doNothing().when(shopService).sellItem(anyInt(), anyDouble());
        shopService.sell(new ItemQuantity(new Item(1, "Item1", 10.0, null, null), 1));

        // Act
        shopService.pay();

        // Assert
        assertNull(shopService.getCurrentReceipt());
        assertNotNull(shop.getReceipts().get(0));
        assertNotNull(shop.getReceipts().get(0).getPurchaseDate());
    }

    @Test
    public void whenPay_thenAddReceiptAndSoldItemsMethodsAreCalled() {
        // Arrange
        doNothing().when(shopService).sellItem(anyInt(), anyDouble());
        shopService.sell(new ItemQuantity(new Item(1, "Item1", 10.0, null, null), 1));

        // Act
        shopService.pay();

        // Assert
        verify(shopService, times(1)).addSoldItemsToShop(any(Receipt.class));
        verify(shopService, times(1)).addReceiptToShop(any(Receipt.class));
    }

}
