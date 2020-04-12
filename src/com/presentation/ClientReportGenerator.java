package com.presentation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.model.Clients;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * This class is used to generate PDF reports for clients.
 */
public class ClientReportGenerator {
    /**
     * Current number of PDF files that have been generated.
     */
    private static int count = 0;
    /**
     * The clients that are present in the database.
     */
    private List<Clients> clients;

    public ClientReportGenerator(List<Clients> clients) {
        this.clients = clients;
        clients.sort(Clients::compareTo);
    }

    /**
     * Used to generate a PDF file that contains a table with all the clients found in the database.
     */
    public void generateReport() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("ClientReport" + count + ".pdf"));
            count++;
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
        document.open();
        PdfPTable table = new PdfPTable(4);
        addTableHeader(table);
        for(Clients currentClient : clients) {
            table.addCell(String.valueOf(clients.indexOf(currentClient) + 1));
            table.addCell(currentClient.getFirstName());
            table.addCell(currentClient.getLastName());
            table.addCell(currentClient.getAddress());
        }
        try {
            document.add(table);
        } catch (DocumentException ignored) {
        }
        document.close();
    }

    /**
     * Adds a header to the table.
     * @param table The table that will be placed inside the PDF file.
     */
    private void addTableHeader(PdfPTable table) {
        table.addCell("Client ID");
        table.addCell("First name");
        table.addCell("Last name");
        table.addCell("Address");
    }
}
