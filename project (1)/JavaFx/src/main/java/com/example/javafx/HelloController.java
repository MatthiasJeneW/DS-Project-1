package com.example.javafx;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.springframework.web.client.RestTemplate;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Logger;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private TextField enterID;

    private String customerId;
    private RestTemplate restTemplate;
    private ScheduledService<Boolean> scheduledService;
    private static final Logger logger = Logger.getLogger(HelloController.class.getName());

    public HelloController() {
        restTemplate = new RestTemplate();
        scheduledService = new ScheduledService<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        String url = "http://localhost:8081/invoices/" + customerId;
                        logger.info("Checking invoice status for URL: " + url);
                        try {
                            String response = restTemplate.getForObject(url, String.class);
                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    }
                };
            }
        };
        scheduledService.setPeriod(Duration.seconds(5));
        scheduledService.setOnSucceeded(event -> {
            if (scheduledService.getValue()) {
                welcomeText.setText("Invoice is ready for customer ID: " + customerId);
                scheduledService.cancel();
                openPDF("FileStorage/Customer_" + customerId + "_" + LocalDate.now() + ".pdf");
            }
        });
    }

    @FXML
    protected void generateInvoiceButton() {
        customerId = enterID.getText();
        if (!customerId.isEmpty()) {
            String url = "http://localhost:8081/invoices/" + customerId;
            logger.info("Generating invoice for URL: " + url);
            restTemplate.postForObject(url, null, String.class);
            welcomeText.setText("Invoice generation started for customer ID: " + customerId);
            scheduledService.restart();
        } else {
            welcomeText.setText("Please enter a valid customer ID.");
        }
    }

    private void openPDF(String filePath) {
        if (Desktop.isDesktopSupported()) {
            try {
                File pdfFile = new File(filePath);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    welcomeText.setText("Invoice file not found.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
