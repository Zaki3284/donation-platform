package com.donationplatform.dto;

import java.util.List;

public class StatisticsDTO {
    private Double totalFundsRaised;
    private Integer totalDonationsCount;
    private Integer activeCampaignsCount;
    private List<MonthlyDonation> donationsByMonth;
    private List<CampaignPerformance> campaignPerformance;

    public StatisticsDTO() {}

    public Double getTotalFundsRaised() {
        return totalFundsRaised;
    }

    public void setTotalFundsRaised(Double totalFundsRaised) {
        this.totalFundsRaised = totalFundsRaised;
    }

    public Integer getTotalDonationsCount() {
        return totalDonationsCount;
    }

    public void setTotalDonationsCount(Integer totalDonationsCount) {
        this.totalDonationsCount = totalDonationsCount;
    }

    public Integer getActiveCampaignsCount() {
        return activeCampaignsCount;
    }

    public void setActiveCampaignsCount(Integer activeCampaignsCount) {
        this.activeCampaignsCount = activeCampaignsCount;
    }

    public List<MonthlyDonation> getDonationsByMonth() {
        return donationsByMonth;
    }

    public void setDonationsByMonth(List<MonthlyDonation> donationsByMonth) {
        this.donationsByMonth = donationsByMonth;
    }

    public List<CampaignPerformance> getCampaignPerformance() {
        return campaignPerformance;
    }

    public void setCampaignPerformance(List<CampaignPerformance> campaignPerformance) {
        this.campaignPerformance = campaignPerformance;
    }

    // Inner classes
    public static class MonthlyDonation {
        private String month;
        private Double amount;

        public MonthlyDonation() {}

        public MonthlyDonation(String month, Double amount) {
            this.month = month;
            this.amount = amount;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }

    public static class CampaignPerformance {
        private String name;
        private Double percentage;

        public CampaignPerformance() {}

        public CampaignPerformance(String name, Double percentage) {
            this.name = name;
            this.percentage = percentage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }
}