package com.presentation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.model.Clients;
import com.model.Orders;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * This class is used to generate PDF reports for orders.
 */
public class OrderReportGenerator {
    /**
     * Current number of PDF files that have been generated.
     */
    private static int count = 0;
    /**
     * The list of orders.
     */
    private List<Orders> orders;
    /**
     * The list of clients.
     */
    private List<Clients> clients;

    public OrderReportGenerator(List<Orders> orders, List<Clients> clients) {
        this.orders = orders;
        this.clients = clients;
    }
    /**
     * Used to generate a PDF file that contains a table with all the orders found in the database.
     */
    public void generateReport() {
        String currentClient;
       for(Orders currentOrder : orders) {
           Document document = new Document();
           currentClient = getClientName(currentOrder);
           try {
               PdfWriter.getInstance(document, new FileOutputStream( currentClient + " order report" + count + ".pdf"));
               count++;
           } catch (FileNotFoundException | DocumentException ignored) {
           }
           document.open();
           PdfPTable table = new PdfPTable(4);
           if(currentOrder.getOk() == 0) {
               Font font = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
               Chunk chunk = new Chunk("The order could not be completed. Not enough products on stock.", font);
               try {
                   document.add(chunk);
               } catch (DocumentException ignored) {
               }
           }
           else {
               addTableHeader(table);
               table.addCell(String.valueOf(orders.indexOf(currentOrder) + 1));
               table.addCell(currentClient);
               table.addCell(currentOrder.getProductName());
               table.addCell("" + currentOrder.getTotalPrice());
               try{
                   document.add(table);
               } catch (DocumentException ignored) {
               }
           }
           document.close();
       }
    }

    /**
     * Gets the name of the client that placed the order.
     * @param currentOrder The current order.
     * @return The name of the client that placed the order.
     */
    private String getClientName(Orders currentOrder) {
        for(Clients currentClient : clients) {
            if(currentOrder.getClientID() == currentClient.getId()) {
                return currentClient.getFirstName() + " "  + currentClient.getLastName();
            }
        }
        return "Not found";
    }
    /**
     * Adds a header to the table.
     * @param table The table that will be placed inside the PDF file.
     */
    private void addTableHeader(PdfPTable table) {
        table.addCell("Order ID");
        table.addCell("Client full name");
        table.addCell("Product name");
        table.addCell("Total price");
    }
}
