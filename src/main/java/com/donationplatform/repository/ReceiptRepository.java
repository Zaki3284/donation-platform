package com.donationplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.donationplatform.entity.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Receipt findByDonationId(Long donationId);
}
