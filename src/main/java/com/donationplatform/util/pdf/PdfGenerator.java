package com.donationplatform.util.pdf;

import org.springframework.stereotype.Component;
import com.donationplatform.entity.Donation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class PdfGenerator {

    // Génère le PDF et retourne le chemin du fichier (simulation pour l'instant)
    public String generate(Donation donation) {
        // Pour l’instant : simulation de génération PDF
        // Plus tard : iText / OpenPDF / Flying Saucer
        return "receipts/receipt_donation_" + donation.getId() + ".pdf";
    }

    // Lit le fichier PDF et retourne son contenu en byte[]
    public byte[] getPdfBytes(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                // Si le fichier n'existe pas, on peut générer un PDF vide ou lancer une exception
                throw new RuntimeException("Le fichier PDF n'existe pas : " + filePath);
            }
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du PDF : " + e.getMessage(), e);
        }
    }
}
