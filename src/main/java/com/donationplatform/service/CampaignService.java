package com.donationplatform.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.donationplatform.entity.Campaign;
import com.donationplatform.entity.CampaignStatus;
import com.donationplatform.repository.CampaignRepository;
import com.donationplatform.exception.BadRequestException;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public Campaign create(Campaign campaign) {
        campaign.setStatut(CampaignStatus.ACTIVE);
        return campaignRepository.save(campaign);
    }

    public Campaign getById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Campaign not found"));
    }

    public void checkIfCampaignIsActive(Campaign campaign) {
        if (campaign.getStatut() == CampaignStatus.TERMINEE ||
                campaign.getDateFin().isBefore(LocalDate.now())) {
            throw new BadRequestException("Campaign is finished");
        }
    }

    public List<Campaign> getActiveCampaigns() {
        return campaignRepository.findByStatut(CampaignStatus.ACTIVE);
    }
}
