package com.example.pdfgenerator.service;

import com.example.pdfgenerator.config.RabbitMQConfig;
import com.example.pdfgenerator.entity.CustomerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PDFGenServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PDFGenService pdfGenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void pdfGenerate_createsPDF() throws IOException {
        // Arrange
        String message = "User: 1\n" +
                "The customer with the ID:1,used 71.100006,on the charging station1\n" +
                "The customer with the ID:1,used 182.3,on the charging station2\n" +
                "The customer with the ID:1,used 167.5,on the charging station3\n" +
                "Total Cost: 294.20€";

        CustomerEntity customerEntity = new CustomerEntity(1, "John", "Doe");
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt())).thenReturn(customerEntity);

        // Act
        pdfGenService.pdfGenerate(message);

        // Assert
        String expectedFileName = "project (1)/FileStorage/Customer_1_" + LocalDate.now() + ".pdf";
        File pdfFile = new File(expectedFileName);
        assertTrue(pdfFile.exists());

        // Clean up
        //pdfFile.delete();
    }

    @Test
    void pdfGenerate_queriesDatabase() {
        // Arrange
        String message = "User: 1\n" +
                "The customer with the ID:1,used 71.100006,on the charging station1\n" +
                "The customer with the ID:1,used 182.3,on the charging station2\n" +
                "The customer with the ID:1,used 167.5,on the charging station3\n" +
                "Total Cost: 294.20€";

        CustomerEntity customerEntity = new CustomerEntity(1, "John", "Doe");
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt())).thenReturn(customerEntity);

        // Act
        pdfGenService.pdfGenerate(message);

        // Assert
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(RowMapper.class), eq(1));
    }
}
