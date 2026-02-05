package com.donationplatform.service;

import com.donationplatform.entity.Campaign;
import com.donationplatform.entity.CampaignStatus;
import com.donationplatform.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public List<Campaign> getActiveCampaigns() {
        return campaignRepository.findByStatut(CampaignStatus.ACTIVE);
    }

    public Optional<Campaign> getCampaignById(Long id) {
        return campaignRepository.findById(id);
    }

    public Campaign createCampaign(Campaign campaign) {
        if (campaign.getMontantCollecte() == null) {
            campaign.setMontantCollecte(0.0);
        }
        return campaignRepository.save(campaign);
    }

    public Campaign updateCampaign(Long id, Campaign updatedCampaign) {
        return campaignRepository.findById(id)
                .map(campaign -> {
                    if (updatedCampaign.getTitre() != null) {
                        campaign.setTitre(updatedCampaign.getTitre());
                    }
                    if (updatedCampaign.getDescription() != null) {
                        campaign.setDescription(updatedCampaign.getDescription());
                    }
                    if (updatedCampaign.getObjectifMontant() != null) {
                        campaign.setObjectifMontant(updatedCampaign.getObjectifMontant());
                    }
                    if (updatedCampaign.getMontantCollecte() != null) {
                        campaign.setMontantCollecte(updatedCampaign.getMontantCollecte());
                    }
                    if (updatedCampaign.getDateDebut() != null) {
                        campaign.setDateDebut(updatedCampaign.getDateDebut());
                    }
                    if (updatedCampaign.getDateFin() != null) {
                        campaign.setDateFin(updatedCampaign.getDateFin());
                    }
                    if (updatedCampaign.getStatut() != null) {
                        campaign.setStatut(updatedCampaign.getStatut());
                    }
                    return campaignRepository.save(campaign);
                })
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));
    }

    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
    }
}
