package com.example.springbootapp.controller;

import com.example.springbootapp.config.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class CustomerControllerTest {

    private CustomerController customerController;
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    public void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        customerController = new CustomerController(rabbitTemplate);
    }
    @Test
    void testStartDataCollection() {
        // Arrange
        String customerId = "12345";

        // Act
        ResponseEntity<String> response = customerController.startDataCollection(customerId);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Data collection started for customer ID: " + customerId, response.getBody());

        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.ECHO_CUSTOMER_ID_QUEUE, customerId);
    }
    @Test
    public void testGetCustomerInvoiceExists() {
        int customerId = 123;

        String pathToFileStorage = "./../FileStorage/Customer_" + customerId + "_" + LocalDate.now() + ".pdf";
        Path invoiceFile = Paths.get(pathToFileStorage);

        // Mocking the behavior of Files.exists
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(invoiceFile)).thenReturn(true);

            ResponseEntity<String> response = customerController.getCustomer(customerId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Invoice for customer ID: " + customerId + " exists.", response.getBody());
        }
    }

    @Test
    public void testGetCustomerInvoiceNotExists() {
        int customerId = 123;

        String pathToFileStorage = "./../FileStorage/Customer_" + customerId + "_" + LocalDate.now() + ".pdf";
        Path invoiceFile = Paths.get(pathToFileStorage);

        // Mocking the behavior of Files.exists
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(invoiceFile)).thenReturn(false);

            ResponseEntity<String> response = customerController.getCustomer(customerId);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Invoice not found for customer ID: " + customerId, response.getBody());
        }
    }


}
