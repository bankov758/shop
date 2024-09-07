package org.example.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.example.entities.ItemQuantity;
import org.example.entities.Receipt;
import org.example.services.interfaces.ReceiptService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceiptPrinter {

    private static int fileCounter = 0;

    private static final String FILE_PATH = "E:\\Projects\\shop\\src\\main\\resources\\receipt";

    private static final String FILE_EXT = ".pdf";

    private static final String PRINTED_SUCCESSFULLY = "PDF created successfully at: " + FILE_PATH;

    /**
     * Prints a list of Orders to a PDF document.
     *
     * @param receipts The list of Order objects to be printed.
     */
    public static void printReceipts(List<Receipt> receipts) {
        for (Receipt receipt : receipts) {
            printReceipt(receipt);
        }
    }

    public static void printReceipt(Receipt receipt) {
        fileCounter++;
        ReceiptService receiptService = new ReceiptServiceImpl();
        try (PdfWriter writer = new PdfWriter(FILE_PATH + fileCounter + FILE_EXT)) {
            try (PdfDocument pdf = new PdfDocument(writer)) {
                try (Document document = new Document(pdf)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    Paragraph title = new Paragraph(receipt.getShop().getName())
                            .setFontSize(18)
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER);
                    document.add(title);
                    document.add(new Paragraph("#" + receipt.getId()));
                    document.add(new Paragraph("Cashier: " + receipt.getCashier().getName()));
                    document.add(new Paragraph("Items: "));
                    for (ItemQuantity itemQuantity : receipt.getItemQuantities()) {
                        document.add(new Paragraph(itemQuantity.getItem().getName() +
                                "   " +
                                String.format("%.2f lv", receiptService.getItemPrice(itemQuantity, receipt)) +
                                " X " + itemQuantity.getQuantity()));
                    }
                    document.add(new Paragraph("Total Cost: " +  String.format("%.2f lv", receiptService.getTotalCost(receipt))));
                    document.add(new Paragraph("Purchase Date: " + receipt.getPurchaseDate().format(formatter)));
                }
            }
            System.out.println(PRINTED_SUCCESSFULLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}