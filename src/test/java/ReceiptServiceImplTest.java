import org.example.entities.*;
import org.example.exceptions.ItemOutOfDateException;
import org.example.services.ReceiptServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.of;
import static org.example.helpers.DateHelper.convertLocalDateToDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ReceiptServiceImplTest {

    @Test
    public void whenAddSingleItemQuantityToEmptyReceipt_thenItemIsAdded() {
        // Arrange
        ReceiptServiceImpl receiptService = new ReceiptServiceImpl();
        Receipt receipt = new Receipt();
        Item item = new Item(1, "Item1", 10.0, null, null);
        ItemQuantity itemQuantity = new ItemQuantity(item, 1.0);

        // Act
        receiptService.addItems(itemQuantity, receipt);

        // Assert
        assertEquals(1, receipt.getItemQuantities().size());
        assertEquals(item, receipt.getItemQuantities().get(0).getItem());
    }

    @Test
    public void whenFoodCategoryItemHasValidExpirationDate_thenPriceIsCalculatedCorrectly() {
        // Arrange
        Item item = new Item(1, "Apple", 10.0,
                ItemCategory.FOOD, convertLocalDateToDate(of(2024, 10, 20)));
        ItemQuantity itemQuantity = new ItemQuantity(item, 1);
        Shop shop = new Shop();
        shop.setFoodMarkupPercent(10);
        shop.setFoodDiscountPercent(1);
        Receipt receipt = new Receipt(1, new Cashier(), of(2024, 10, 15).atStartOfDay(),
                List.of(itemQuantity), shop);
        ReceiptServiceImpl receiptService = new ReceiptServiceImpl();

        // Act
        double price = receiptService.getItemPrice(itemQuantity, receipt);

        // Assert
        assertEquals(10.5, price);
    }

    @Test
    public void whenNonFoodCategoryItemHasValidExpirationDate_thenPriceIsCalculatedCorrectly() {
        // Arrange
        Item item = new Item(2, "Shampoo", 20.0, ItemCategory.NON_FOOD,
                convertLocalDateToDate(of(2024, 10, 20)));
        ItemQuantity itemQuantity = new ItemQuantity(item, 1);
        Shop shop = new Shop();
        shop.setNonFoodMarkupPercent(15);
        shop.setNonFoodDiscountPercent(2);
        Receipt receipt = new Receipt(2, new Cashier(),
                of(2024, 10, 15).atStartOfDay(), List.of(itemQuantity), shop);
        ReceiptServiceImpl receiptService = new ReceiptServiceImpl();

        // Act
        double price = receiptService.getItemPrice(itemQuantity, receipt);

        // Assert
        assertEquals(21.0, price);
    }

    @Test
    public void whenFoodCategoryItemHasPastExpirationDate_thenItemOutOfDateExceptionIsThrown() {
        // Arrange
        Item item = new Item(5, "Milk", 1.5, ItemCategory.FOOD,
                convertLocalDateToDate(of(2024, 10, 15)));
        ItemQuantity itemQuantity = new ItemQuantity(item, 1);
        Receipt receipt = new Receipt(5, new Cashier(),
                of(2024, 10, 20).atStartOfDay(), List.of(itemQuantity), new Shop());
        ReceiptServiceImpl receiptService = new ReceiptServiceImpl();

        // Act & Assert
        assertThrows(ItemOutOfDateException.class, () -> {
            receiptService.getItemPrice(itemQuantity, receipt);
        });
    }

    @Test
    public void whenMultipleItemsInReceipt_thenTotalCostIsCalculatedCorrectly() {
        // Arrange
        ReceiptServiceImpl receiptService = spy(new ReceiptServiceImpl());
        doReturn(10.0, 20.0)
                .when(receiptService)
                .getItemPrice(any(ItemQuantity.class), any(Receipt.class));

        Item item1 = new Item(1, "Apple", 1.0, ItemCategory.FOOD,
                convertLocalDateToDate(of(2024, 10, 20)));
        Item item2 = new Item(2, "Banana", 0.5, ItemCategory.FOOD,
                convertLocalDateToDate(of(2024, 10, 20)));
        ItemQuantity itemQuantity1 = new ItemQuantity(item1, 2);
        ItemQuantity itemQuantity2 = new ItemQuantity(item2, 3);
        List<ItemQuantity> itemQuantities = Arrays.asList(itemQuantity1, itemQuantity2);
        Receipt receipt = new Receipt(1, new Cashier(), of(2024, 10, 20).atStartOfDay(),
                itemQuantities, new Shop());

        // Act
        double totalCost = receiptService.getTotalCost(receipt);

        // Assert
        assertEquals(80.0, totalCost);
    }

}
