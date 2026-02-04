package com.donationplatform.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.donationplatform.entity.Donation;
import com.donationplatform.entity.User;
import com.donationplatform.service.DonationService;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @PostMapping("/{campaignId}")
    public Donation donate(
            @PathVariable Long campaignId,
            @RequestParam Double amount,
            @RequestAttribute User user   // injecté via JWT
    ) {
        return donationService.donate(user, campaignId, amount);
    }
}
