package com.safari.app.receipt;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.safari.app.model.CottageReservation;
import com.safari.app.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

/**
 * Generates and stores reservation receipts as PDF files.
 * OpenPDF is used because it is a standard Java PDF-generation library and
 * works well with a Spring Boot 3 / Java 17 application.
 */
@Service
public class ReceiptService {

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Path receiptDirectory;

    public ReceiptService(@Value("${app.receipt-dir:receipts}") String receiptDir) {
        this.receiptDirectory = Paths.get(receiptDir).toAbsolutePath().normalize();
    }

    public Path generateReceipt(CottageReservation reservation, User user) {
        try {
            Files.createDirectories(receiptDirectory);
            Path output = getReceiptPath(reservation.getId());

            try (OutputStream out = Files.newOutputStream(output)) {
                writePdf(out, reservation, user);
            }

            return output;
        } catch (IOException | DocumentException ex) {
            throw new IllegalStateException("Reservation was created, but the PDF receipt could not be generated.", ex);
        }
    }

    public byte[] generateReceiptBytes(CottageReservation reservation, User user) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writePdf(out, reservation, user);
            return out.toByteArray();
        } catch (IOException | DocumentException ex) {
            throw new IllegalStateException("Could not generate the reservation PDF receipt.", ex);
        }
    }

    public Path getReceiptPath(Long reservationId) {
        if (reservationId == null) {
            throw new IllegalArgumentException("Reservation ID is required to locate a receipt.");
        }
        return receiptDirectory.resolve("reservation-receipt-" + reservationId + ".pdf");
    }

    private void writePdf(OutputStream out, CottageReservation reservation, User user)
            throws DocumentException, IOException {
        Document document = new Document(com.lowagie.text.PageSize.A4, 42, 42, 42, 42);
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("LANKA WILD TRAILS"));
        document.add(new Paragraph("Cottage Reservation Receipt"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.15f, 2.85f});

        addRow(table, "Request ID", String.valueOf(reservation.getId()));
        addRow(table, "Request Date", reservation.getCreatedAt() != null ? reservation.getCreatedAt().format(DATE_TIME) : "-");
        addRow(table, "Reservation Code", safe(reservation.getReservationCode()));
        addRow(table, "User Name", user != null ? safe(user.getFullName()) : safe(reservation.getGuestName()));
        addRow(table, "User Email", user != null ? safe(user.getEmail()) : safe(reservation.getGuestEmail()));
        addRow(table, "Cottage Name", reservation.getCottage() != null ? safe(reservation.getCottage().getCottageName()) : "-");
        addRow(table, "Check-in Date", String.valueOf(reservation.getCheckInDate()));
        addRow(table, "Check-out Date", String.valueOf(reservation.getCheckOutDate()));
        addRow(table, "Number of Guests", String.valueOf(reservation.getNumberOfGuests()));
        addRow(table, "Total Price", formatMoney(reservation.getTotalAmount()));
        addRow(table, "Status", safe(reservation.getStatus()));

        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Thank you for choosing Lanka Wild Trails."));
        document.add(new Paragraph("Keep this receipt for your reservation records."));
        document.close();
    }

    private void addRow(PdfPTable table, String label, String value) {
        PdfPCell left = new PdfPCell(new Phrase(label));
        PdfPCell right = new PdfPCell(new Phrase(value));
        left.setPadding(7);
        right.setPadding(7);
        table.addCell(left);
        table.addCell(right);
    }

    private String formatMoney(java.math.BigDecimal value) {
        return value == null ? "0.00" : value.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
