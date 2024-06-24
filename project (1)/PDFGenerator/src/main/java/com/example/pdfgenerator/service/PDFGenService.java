package com.example.pdfgenerator.service;

import com.example.pdfgenerator.config.RabbitMQConfig;
import com.example.pdfgenerator.entity.CustomerEntity;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.ResultSet;
import java.time.LocalDate;

@Service
public class PDFGenService {

    private final JdbcTemplate jdbcTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PDFGenService(@Qualifier("CDB") JdbcTemplate jdbcTemplate, RabbitTemplate rabbitTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.ECHO_OUT_QUEUE_PDF_GEN)
    public void pdfGenerate(String message) {
        System.out.println(message);
        try {
            String[] messageParts = message.split("\n");
            String header = messageParts[0];
            int customerID = Integer.parseInt(header.split("User: ")[1].trim());

            String customerQuery = "SELECT * FROM customer WHERE id = ?";
            CustomerEntity customerData = jdbcTemplate.queryForObject(customerQuery, (ResultSet rs, int rowNum) -> new CustomerEntity(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
            ), customerID);

            String firstName = customerData.getFirstName();
            String lastName = customerData.getLastName();

            // Ensure the directory exists
            String directoryPath = "project (1)/FileStorage";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it does not exist
            }

            String fileName = directoryPath + "/Customer_" + customerID + "_" + LocalDate.now() + ".pdf";
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("Invoice").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(25));
            document.add(new Paragraph("Date: " + LocalDate.now()).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Customer ID: " + customerID).setTextAlignment(TextAlignment.LEFT));
            document.add(new Paragraph("First Name: " + firstName));
            document.add(new Paragraph("Last Name: " + lastName));

            // Add table for kwh details
            Paragraph tableHeader = new Paragraph("Charge Details")
                    .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                    .setFontSize(18)
                    .setBold()
                    .setFontColor(ColorConstants.GREEN);
            document.add(tableHeader);

            Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
            table.addHeaderCell(getHeaderCell("Station ID"));
            table.addHeaderCell(getHeaderCell("KWH Used"));
            table.addHeaderCell(getHeaderCell("Total Cost (€)"));

            for (int i = 1; i < messageParts.length - 1; i++) {
                String[] dataParts = messageParts[i].split(",");
                String stationID = dataParts[2].split("station")[1].trim();
                String kwhUsed = dataParts[1].split(" ")[1].trim();
                float kwh = Float.parseFloat(kwhUsed);
                float cost = kwh * 0.7f;

                table.addCell(stationID);
                table.addCell(kwhUsed);
                table.addCell(String.format("%.2f", cost));
            }

            String totalCostStr = messageParts[messageParts.length - 1].split(": ")[1].replace("€", "").trim();
            document.add(table);
            document.add(new Paragraph("Total KWH Used: " + String.format("%.2f", Float.parseFloat(totalCostStr) / 0.7f)).setTextAlignment(TextAlignment.RIGHT).setBold());
            document.add(new Paragraph("Total Cost: " + totalCostStr + "€").setTextAlignment(TextAlignment.RIGHT).setBold());

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Cell getHeaderCell(String text) {
        return new Cell().add(new Paragraph(text)).setBold().setBackgroundColor(ColorConstants.GRAY);
    }
}
