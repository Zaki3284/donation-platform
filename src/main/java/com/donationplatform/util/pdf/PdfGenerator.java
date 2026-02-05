package com.donationplatform.util.pdf;

import com.donationplatform.entity.Donation;
import org.springframework.stereotype.Component;
@Component
public class PdfGenerator {

    public String generate(Donation donation) {

        // Pour l’instant : simulation de génération PDF
        // Plus tard : iText / OpenPDF / Flying Saucer

        return "receipts/receipt_donation_" + donation.getId() + ".pdf";
    }
}
