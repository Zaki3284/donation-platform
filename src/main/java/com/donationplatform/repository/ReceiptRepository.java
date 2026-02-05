package com.donationplatform.repository;

import com.donationplatform.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Receipt findByDonationId(Long donationId);
}
