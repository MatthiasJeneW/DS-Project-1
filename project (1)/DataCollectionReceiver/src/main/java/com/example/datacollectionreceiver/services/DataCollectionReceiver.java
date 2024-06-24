package com.example.datacollectionreceiver.services;

import com.example.datacollectionreceiver.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DataCollectionReceiver {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public DataCollectionReceiver(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private Map<String, StringBuilder> messageMap = new HashMap<>();
    static final int TOTAL_NUMBER_OF_CHARGING_STATIONS = 3;

    @RabbitListener(queues = RabbitMQConfig.ECHO_OUT_RECEIVER_QUEUE)
    public void getUserMessage(String message) {
        System.out.println("Received USER_ID: " + message);
    }

    @RabbitListener(queues = RabbitMQConfig.ECHO_OUT_STATION_DATA_QUEUE)
    public void sortIncomingData(String message) {
        System.out.println(message);
        String[] messageParts = message.split(",");

        if (messageParts.length < 3) {
            return;
        }

        String customerIDPart = messageParts[0];
        String kwhUsedPart = messageParts[1];
        String dbUrlPart = messageParts[2];

        String customerID = customerIDPart.split(":")[1].trim();
        String kwhUsed = kwhUsedPart.split(" ")[1].trim();
        String dbUrl = dbUrlPart.split("station")[1].trim();


        // Check if we have a message builder for this user
        messageMap.putIfAbsent(customerID, new StringBuilder());

        // Append this station's data to the user's data
        StringBuilder userData = messageMap.get(customerID);
        userData.append(message).append("\n");

        // Check if we've received data from all stations
        if (userData.toString().split("\n").length == TOTAL_NUMBER_OF_CHARGING_STATIONS) {
            forwardDataToPDFGenerator(customerID, userData.toString());
            messageMap.remove(customerID); // Clean up the map
        }
    }

    public void forwardDataToPDFGenerator(String customerID, String data) {
        float totalCost = 0;
        float costPerKwh = 0.7f;

        String[] lines = data.split("\n");
        StringBuilder dataWithCost = new StringBuilder();

        for (String line : lines) {
            String[] messageParts = line.split(",");
            String kwhUsedPart = messageParts[1];
            float kwhUsed = Float.parseFloat(kwhUsedPart.split(" ")[1].trim());
            totalCost += kwhUsed * costPerKwh;
            dataWithCost.append(line).append("\n");
        }

        String message = "User: " + customerID + "\n" + dataWithCost.toString() + "Total cost: " + totalCost + "€";
        rabbitTemplate.convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_PDF_GEN, message);
        System.out.println("Forwarded data to PDF Generator: " + message);
    }
}
