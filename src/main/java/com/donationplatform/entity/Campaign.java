package com.donationplatform.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "campaigns")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(length = 1000)
    private String description;

    private Double objectifMontant;

    private Double montantCollecte = 0.0;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private CampaignStatus statut;

    @OneToMany(mappedBy = "campaign")
    private List<Donation> donations;

    // ===== Constructors =====
    public Campaign() {}

    // ===== Méthodes métier =====
    public boolean estTerminee() {
        return statut == CampaignStatus.TERMINEE || LocalDate.now().isAfter(dateFin);
    }

    // ===== Getters & Setters =====
    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getObjectifMontant() {
        return objectifMontant;
    }

    public void setObjectifMontant(Double objectifMontant) {
        this.objectifMontant = objectifMontant;
    }

    public Double getMontantCollecte() {
        return montantCollecte;
    }

    public void setMontantCollecte(Double montantCollecte) {
        this.montantCollecte = montantCollecte;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public CampaignStatus getStatut() {
        return statut;
    }

    public void setStatut(CampaignStatus statut) {
        this.statut = statut;
    }
}
