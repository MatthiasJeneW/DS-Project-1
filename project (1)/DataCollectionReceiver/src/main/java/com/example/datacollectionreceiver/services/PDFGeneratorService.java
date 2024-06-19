package com.example.datacollectionreceiver.services;

import com.example.datacollectionreceiver.entity.MessageEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PDFGeneratorService {

    private final RabbitTemplate rabbitTemplate;


    public PDFGeneratorService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
}
