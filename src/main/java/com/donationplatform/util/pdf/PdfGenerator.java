package com.donationplatform.util.pdf;

import org.springframework.stereotype.Component;
import com.donationplatform.entity.Donation;

@Component
public class PdfGenerator {

    public String generate(Donation donation) {

        // Pour l’instant : simulation de génération PDF
        // Plus tard : iText / OpenPDF / Flying Saucer

        return "receipts/receipt_donation_" + donation.getId() + ".pdf";
    }
}
