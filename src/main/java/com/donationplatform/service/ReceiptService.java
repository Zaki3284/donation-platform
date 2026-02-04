package com.donationplatform.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.donationplatform.entity.Donation;
import com.donationplatform.entity.Receipt;
import com.donationplatform.repository.ReceiptRepository;
import com.donationplatform.util.pdf.PdfGenerator;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final PdfGenerator pdfGenerator;

    public void generateReceipt(Donation donation) {

        String pdfPath = pdfGenerator.generate(donation);

        Receipt receipt = new Receipt();
        receipt.setDonation(donation);
        receipt.setDateGeneration(LocalDateTime.now());
        receipt.setFilePath(pdfPath);

        receiptRepository.save(receipt);
    }

    public Receipt getByDonationId(Long donationId) {
        return receiptRepository.findByDonationId(donationId);
    }
}
