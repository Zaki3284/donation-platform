package com.donationplatform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.donationplatform.entity.Campaign;
import com.donationplatform.service.CampaignService;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping
    public Campaign create(@RequestBody Campaign campaign) {
        return campaignService.create(campaign);
    }

    @GetMapping("/active")
    public List<Campaign> getActiveCampaigns() {
        return campaignService.getActiveCampaigns();
    }
}
