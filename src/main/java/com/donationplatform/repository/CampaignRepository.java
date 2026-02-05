package com.donationplatform.repository;

import com.donationplatform.entity.Campaign;
import com.donationplatform.entity.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByStatut(CampaignStatus statut);
}
