package com.donationplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.donationplatform.entity.Campaign;
import com.donationplatform.entity.CampaignStatus;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByStatut(CampaignStatus status);
}
