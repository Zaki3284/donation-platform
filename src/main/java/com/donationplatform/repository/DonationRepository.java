package com.donationplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.donationplatform.entity.Donation;
import com.donationplatform.entity.User;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    // Retourne tous les dons faits par un utilisateur
    List<Donation> findByUser(User user);

    // Retourne tous les dons d’une campagne
    List<Donation> findByCampaignId(Long campaignId);

    // Somme totale des montants de tous les dons
    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Donation d")
    Double totalAmount();
}
