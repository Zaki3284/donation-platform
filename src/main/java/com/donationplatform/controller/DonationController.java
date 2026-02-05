package com.donationplatform.controller;

import com.donationplatform.dto.*;
import com.donationplatform.entity.Campaign;
import com.donationplatform.entity.Donation;
import com.donationplatform.entity.User;
import com.donationplatform.service.CampaignService;
import com.donationplatform.service.DonationService;
import com.donationplatform.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "*")
public class DonationController {

    private final DonationService donationService;
    private final UserService userService;
    private final CampaignService campaignService;

    public DonationController(DonationService donationService,
                              UserService userService,
                              CampaignService campaignService) {
        this.donationService = donationService;
        this.userService = userService;
        this.campaignService = campaignService;
    }

    // Create Donation (DONOR only)
    @PostMapping
    @PreAuthorize("hasAuthority('DONATEUR')")
    public ResponseEntity<?> createDonation(@RequestBody DonationCreateRequest request,
                                            Authentication authentication) {
        try {
            // Get authenticated user
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Get campaign
            Campaign campaign = campaignService.getCampaignById(Long.parseLong(request.getCampaignId()))
                    .orElseThrow(() -> new RuntimeException("Campaign not found"));

            // Create donation
            Donation donation = new Donation();
            donation.setMontant(request.getAmount());
            donation.setUser(user);
            donation.setCampaign(campaign);
            donation.setDateDonation(LocalDateTime.now());

            Donation saved = donationService.createDonation(donation);

            // Update campaign amount
            campaign.setMontantCollecte(campaign.getMontantCollecte() + request.getAmount());
            campaignService.updateCampaign(campaign.getId(), campaign);

            // Build response
            DonationDTO response = convertToDTO(saved);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to create donation: " + e.getMessage()));
        }
    }

    // Get My Donations (DONOR only)
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('DONATEUR')")
    public ResponseEntity<?> getMyDonations(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Donation> donations = donationService.getDonationsByUserId(user.getId());
            List<DonationDTO> response = donations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to fetch donations"));
        }
    }

    // Helper method to convert Entity to DTO
    private DonationDTO convertToDTO(Donation donation) {
        DonationDTO dto = new DonationDTO();
        dto.setId(donation.getId().toString());
        dto.setAmount(donation.getMontant());
        dto.setDonorId(donation.getUser().getId().toString());
        dto.setDonorName(donation.getUser().getNom());
        dto.setCampaignId(donation.getCampaign().getId().toString());
        dto.setCampaignTitle(donation.getCampaign().getTitre());
        dto.setDate(donation.getDateDonation().format(DateTimeFormatter.ISO_DATE_TIME));
        dto.setReceiptId("REC-" + donation.getId());
        return dto;
    }
}