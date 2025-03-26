package com.coworking.management.controller;

import com.coworking.management.entity.Invoice;
import com.coworking.management.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Invoice>> getUserInvoices(@PathVariable Long userId) {
        return ResponseEntity.ok(invoiceService.getUserInvoices(userId));
    }

    @GetMapping(value = "/{invoiceId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable Long invoiceId) {
        try {
            byte[] pdf = invoiceService.getInvoicePdf(invoiceId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "invoice_" + invoiceId + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error generating PDF: " + e.getMessage(),
                e
            );
        }
    }
}