package com.donationplatform.service;

import com.donationplatform.dto.StatisticsDTO;
import com.donationplatform.entity.Campaign;
import com.donationplatform.entity.CampaignStatus;
import com.donationplatform.entity.Donation;
import com.donationplatform.repository.CampaignRepository;
import com.donationplatform.repository.DonationRepository;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;

    public StatisticsService(DonationRepository donationRepository,
                             CampaignRepository campaignRepository) {
        this.donationRepository = donationRepository;
        this.campaignRepository = campaignRepository;
    }

    public StatisticsDTO getDashboardStatistics() {
        StatisticsDTO stats = new StatisticsDTO();

        // Total funds raised
        List<Donation> allDonations = donationRepository.findAll();
        Double totalFunds = allDonations.stream()
                .mapToDouble(Donation::getMontant)
                .sum();
        stats.setTotalFundsRaised(totalFunds);

        // Total donations count
        stats.setTotalDonationsCount(allDonations.size());

        // Active campaigns count
        List<Campaign> activeCampaigns = campaignRepository.findByStatut(CampaignStatus.ACTIVE);
        stats.setActiveCampaignsCount(activeCampaigns.size());

        // Donations by month
        Map<String, Double> monthlyDonations = new HashMap<>();
        allDonations.forEach(donation -> {
            String month = donation.getDateDonation()
                    .getMonth()
                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            monthlyDonations.merge(month, donation.getMontant(), Double::sum);
        });

        List<StatisticsDTO.MonthlyDonation> donationsByMonth = monthlyDonations.entrySet().stream()
                .map(entry -> new StatisticsDTO.MonthlyDonation(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        stats.setDonationsByMonth(donationsByMonth);

        // Campaign performance
        List<StatisticsDTO.CampaignPerformance> campaignPerformance = activeCampaigns.stream()
                .map(campaign -> {
                    Double percentage = (campaign.getMontantCollecte() / campaign.getObjectifMontant()) * 100;
                    return new StatisticsDTO.CampaignPerformance(
                            campaign.getTitre(),
                            Math.round(percentage * 100.0) / 100.0
                    );
                })
                .collect(Collectors.toList());
        stats.setCampaignPerformance(campaignPerformance);

        return stats;
    }
}