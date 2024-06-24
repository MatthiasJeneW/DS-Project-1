package com.example.pdfgenerator.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.port}")
    private int RABBITMQ_PORT;

    public static final String ECHO_OUT_QUEUE_PDF_GEN = "generatePDF";

    @Bean
    public Queue GENERATING_PDF_QUEUE(){return new Queue(ECHO_OUT_QUEUE_PDF_GEN, false);   }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setPort(RABBITMQ_PORT);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        //rabbitTemplate.setDefaultReceiveQueue(String.valueOf(ECHO_CUSTOMER_ID_QUEUE));
        return rabbitTemplate;
    }
}

