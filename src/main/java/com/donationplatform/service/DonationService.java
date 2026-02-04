package com.donationplatform.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.donationplatform.entity.Campaign;
import com.donationplatform.entity.Donation;
import com.donationplatform.entity.User;
import com.donationplatform.repository.DonationRepository;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final CampaignService campaignService;
    private final ReceiptService receiptService;

    public Donation donate(User user, Long campaignId, Double amount) {

        Campaign campaign = campaignService.getById(campaignId);
        campaignService.checkIfCampaignIsActive(campaign);

        Donation donation = new Donation();
        donation.setMontant(amount);
        donation.setDateDonation(LocalDateTime.now());
        donation.setUser(user);
        donation.setCampaign(campaign);

        Donation savedDonation = donationRepository.save(donation);

        receiptService.generateReceipt(savedDonation);

        return savedDonation;
    }

    public Donation findById(Long id) {
        return donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
    }

    public List<Donation> findByUser(User user) {
        return donationRepository.findByUser(user);
    }

    public long countAll() {
        return donationRepository.count();
    }

    public double totalAmount() {
        Double total = donationRepository.totalAmount();
        return total != null ? total : 0;
    }
}
