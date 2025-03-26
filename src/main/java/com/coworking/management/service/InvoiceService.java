package com.coworking.management.service;

import com.coworking.management.entity.*;
import com.coworking.management.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private PdfGenerationService pdfService;

    public Invoice generateInvoice(Subscription subscription, User user) {
        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setSubscription(subscription);
        invoice.setIssueDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(15));
        invoice.setAmount(subscription.getPrice());
        invoice.setTaxAmount(subscription.getPrice() * 0.2);
        invoice.setStatus("UNPAID");
        invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
        
        InvoiceItem item = new InvoiceItem();
        item.setDescription("Abonnement " + subscription.getName());
        item.setQuantity(1);
        item.setUnitPrice(subscription.getPrice());
        item.setTotal(subscription.getPrice());
        invoice.setItems(List.of(item));
        
        invoice.setPdfPath(pdfService.generateInvoicePdf(invoice));
        return invoiceRepository.save(invoice);
    }

    public byte[] getInvoicePdf(Long invoiceId) throws IOException {
        Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        Path path = Paths.get(invoice.getPdfPath());
        if (!Files.exists(path)) {
            throw new IOException("PDF file not found for invoice: " + invoiceId);
        }
        
        return Files.readAllBytes(path);
    }

    public List<Invoice> getUserInvoices(Long userId) {
        return invoiceRepository.findByUserId(userId);
    }
}