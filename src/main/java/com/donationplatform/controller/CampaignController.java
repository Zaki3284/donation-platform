package com.donationplatform.controller;

import com.donationplatform.dto.*;
import com.donationplatform.entity.Campaign;
import com.donationplatform.entity.CampaignStatus;
import com.donationplatform.service.CampaignService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/campaigns")
@CrossOrigin(origins = "*")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    // List All Campaigns (Public)
    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {
        List<Campaign> campaigns = campaignService.getAllCampaigns();
        List<CampaignDTO> response = campaigns.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // List Active Campaigns Only (Public)
    @GetMapping("/active")
    public ResponseEntity<List<CampaignDTO>> getActiveCampaigns() {
        List<Campaign> campaigns = campaignService.getActiveCampaigns();
        List<CampaignDTO> response = campaigns.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Get Campaign Details (Public)
    @GetMapping("/{id}")
    public ResponseEntity<?> getCampaignById(@PathVariable Long id) {
        try {
            Campaign campaign = campaignService.getCampaignById(id)
                    .orElseThrow(() -> new RuntimeException("Campaign not found"));
            return ResponseEntity.ok(convertToDTO(campaign));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Create Campaign (ADMIN only)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createCampaign(@RequestBody CampaignCreateRequest request) {
        try {
            Campaign campaign = new Campaign();
            campaign.setTitre(request.getTitle());
            campaign.setDescription(request.getDescription());
            campaign.setObjectifMontant(request.getGoalAmount());
            campaign.setMontantCollecte(0.0); // Always start at 0
            campaign.setDateDebut(request.getStartDate());
            campaign.setDateFin(request.getEndDate());
            campaign.setStatut(CampaignStatus.valueOf(request.getStatus()));
            campaign.setImageUrl(request.getImageUrl());

            Campaign saved = campaignService.createCampaign(campaign);
            return ResponseEntity.ok(convertToDTO(saved));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to create campaign: " + e.getMessage()));
        }
    }

    // Delete Campaign (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteCampaign(@PathVariable Long id) {
        try {
            campaignService.deleteCampaign(id);
            return ResponseEntity.ok(new MessageResponse("Deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to delete campaign"));
        }
    }

    // Helper method to convert Entity to DTO
    private CampaignDTO convertToDTO(Campaign campaign) {
        CampaignDTO dto = new CampaignDTO();
        dto.setId(campaign.getId().toString());
        dto.setTitle(campaign.getTitre());
        dto.setDescription(campaign.getDescription());
        dto.setGoalAmount(campaign.getObjectifMontant());
        dto.setCurrentAmount(campaign.getMontantCollecte());
        dto.setStartDate(campaign.getDateDebut().toString());
        dto.setEndDate(campaign.getDateFin().toString());
        dto.setImageUrl(campaign.getImageUrl() != null && !campaign.getImageUrl().isBlank()
                ? campaign.getImageUrl()
                : "https://via.placeholder.com/400x300");
        dto.setStatus(campaign.getStatut().name());
        return dto;
    }
}
