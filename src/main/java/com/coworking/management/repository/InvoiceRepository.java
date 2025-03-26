package com.coworking.management.repository;

import com.coworking.management.entity.Invoice;
import com.coworking.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUser(User user);
    List<Invoice> findByUserId(Long userId);
}