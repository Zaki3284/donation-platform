package com.donationplatform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.donationplatform.entity.Receipt;
import com.donationplatform.service.ReceiptService;
@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping("/donation/{donationId}")
    public Receipt getReceipt(@PathVariable Long donationId) {
        return receiptService.getByDonationId(donationId);
    }
}
