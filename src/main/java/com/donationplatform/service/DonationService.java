package com.donationplatform.service;

import com.donationplatform.entity.Donation;
import com.donationplatform.repository.DonationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class DonationService {

    private final DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation createDonation(Donation donation) {
        if (donation.getDateDonation() == null) {
            donation.setDateDonation(LocalDateTime.now());
        }
        return donationRepository.save(donation);
    }

    public List<Donation> getDonationsByUserId(Long userId) {
        return donationRepository.findByUserId(userId);
    }

    public List<Donation> getDonationsByCampaignId(Long campaignId) {
        return donationRepository.findByCampaignId(campaignId);
    }

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public Optional<Donation> getDonationById(Long id) {
        return donationRepository.findById(id);
    }
}
