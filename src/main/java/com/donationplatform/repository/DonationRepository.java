package com.donationplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.donationplatform.entity.Donation;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByUserId(Long userId);
    List<Donation> findByCampaignId(Long campaignId);
}
