package com.presentation;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.model.Products;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
/**
 * This class is used to generate PDF reports for products.
 */
public class ProductReportGenerator {
    /**
     * The list of products.
     */
    private List<Products> products;
    /**
     * Current number of PDF files that have been generated.
     */
    private static int count = 0;

    public ProductReportGenerator(List<Products> products) {
        this.products = products;
        products.sort(Products::compareTo);
    }
    /**
     * Used to generate a PDF file that contains a table with all the products found in the database.
     */
    public void generateReport() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("ProductReport" + count + ".pdf"));
            count++;
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
        document.open();
        PdfPTable table = new PdfPTable(4);
        addTableHeader(table);
        for(Products currentProduct : products) {
            table.addCell(String.valueOf(products.indexOf(currentProduct) + 1));
            table.addCell(currentProduct.getProductName());
            table.addCell("" + currentProduct.getPrice());
            table.addCell("" + currentProduct.getQuantity());
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
        table.addCell("Product ID");
        table.addCell("Product name");
        table.addCell("Price");
        table.addCell("Quantity");
    }
}