package com.example.datacollectionreceiver.services;

import com.example.datacollectionreceiver.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DataCollectionReceiverTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataCollectionReceiver dataCollectionReceiver;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserMessage() {
        // Arrange
        String message = "USER_ID:1";

        // Act
        dataCollectionReceiver.getUserMessage(message);

        // Assert
        verifyNoInteractions(rabbitTemplate, objectMapper);
    }

    @Test
    public void testSortIncomingData() {
        String message1 = "The customer with the ID:1,used 71.100006,on the charging station1";
        String message2 = "The customer with the ID:1,used 182.3,on the charging station2";
        String message3 = "The customer with the ID:1,used 167.5,on the charging station3";

        dataCollectionReceiver.sortIncomingData(message1);
        dataCollectionReceiver.sortIncomingData(message2);
        dataCollectionReceiver.sortIncomingData(message3);

        String expectedMessage = "User: 1\n" +
                "The customer with the ID:1,used 71.100006,on the charging station1\n" +
                "The customer with the ID:1,used 182.3,on the charging station2\n" +
                "The customer with the ID:1,used 167.5,on the charging station3\n" +
                "Total cost: 294.63€";

        verify(rabbitTemplate, times(1)).convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_PDF_GEN, expectedMessage);
    }

    @Test
    public void testForwardDataToPDFGenerator() {
        String customerID = "1";
        String data = "The customer with the ID:1,used 71.100006,on the charging station1\n" +
                "The customer with the ID:1,used 182.3,on the charging station2\n" +
                "The customer with the ID:1,used 167.5,on the charging station3\n";

        dataCollectionReceiver.forwardDataToPDFGenerator(customerID, data);

        float totalCost = (71.100006f + 182.3f + 167.5f) * 0.7f;
        String expectedMessage = "User: 1\n" + data + "Total cost: " + totalCost + "€";

        verify(rabbitTemplate, times(1)).convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_PDF_GEN, expectedMessage);
    }
}
