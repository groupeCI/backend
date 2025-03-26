package com.coworking.management.service;

import com.coworking.management.entity.Invoice;
import com.coworking.management.entity.InvoiceItem;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationService {

    private static final String INVOICE_DIR = "./invoices/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generateInvoicePdf(Invoice invoice) {
        try {
            // Créer le répertoire s'il n'existe pas
            Path directory = Paths.get(INVOICE_DIR);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // Générer un nom de fichier unique
            String filename = String.format("invoice_%s_%d.pdf", 
                invoice.getIssueDate().format(DATE_FORMAT), 
                invoice.getId());
            
            Path filePath = directory.resolve(filename);

            // Ici vous devriez utiliser une vraie librairie PDF comme Apache PDFBox ou iText
            String content = generateInvoiceContent(invoice);
            Files.write(filePath, content.getBytes());

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PDF for invoice: " + invoice.getId(), e);
        }
    }

    private String generateInvoiceContent(Invoice invoice) {
        // Implémentation simplifiée - à remplacer par un vrai générateur PDF
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice Number: ").append(invoice.getInvoiceNumber()).append("\n");
        sb.append("Date: ").append(invoice.getIssueDate()).append("\n");
        sb.append("Due Date: ").append(invoice.getDueDate()).append("\n");
        sb.append("\nClient: ").append(invoice.getUser().getFullName()).append("\n");
        sb.append("\nItems:\n");
        
        for (InvoiceItem item : invoice.getItems()) {
            sb.append("- ").append(item.getDescription())
              .append(": ").append(item.getQuantity())
              .append(" x ").append(item.getUnitPrice())
              .append(" = ").append(item.getTotal()).append("\n");
        }
        
        sb.append("\nSubtotal: ").append(invoice.getAmount()).append("\n");
        sb.append("Tax: ").append(invoice.getTaxAmount()).append("\n");
        sb.append("Total: ").append(invoice.getAmount() + invoice.getTaxAmount()).append("\n");
        
        return sb.toString();
    }
}