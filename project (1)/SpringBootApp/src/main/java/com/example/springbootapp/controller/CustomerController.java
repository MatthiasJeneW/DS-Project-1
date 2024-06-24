package com.example.springbootapp.controller;

import com.example.springbootapp.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@RestController
public class CustomerController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CustomerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/invoices/{customer-id}")
    public ResponseEntity<String> startDataCollection(@PathVariable("customer-id") String customerId) {
        // Send a message to the data collection queue to start the process
        System.out.println("Start collection job");
        rabbitTemplate.convertAndSend(RabbitMQConfig.ECHO_CUSTOMER_ID_QUEUE, customerId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Data collection started for customer ID: " + customerId);
    }

    @GetMapping("/invoices/{customerId}")
    public ResponseEntity<String> getCustomer(@PathVariable("customerId") int id) {
        // Construct the file path
        String pathToFileStorage = "./../FileStorage/Customer_" + id + "_" + LocalDate.now() + ".pdf";
        Path invoiceFile = Paths.get(pathToFileStorage);

        // Check if the file exists
        if (Files.exists(invoiceFile)) {
            System.out.println("File exists");
            return ResponseEntity.ok("Invoice for customer ID: " + id + " exists.");
        } else {
            System.out.println("File does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invoice not found for customer ID: " + id);
        }
    }
}
