package com.example.datacollectionreceiver.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;



@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String rabbitmq_Host;

    @Value("${spring.rabbitmq.port}")
    private int RABBITMQ_PORT;

    @Value("${spring.rabbitmq.username}")
    private String rabbitmq_Username;

    @Value("${spring.rabbitmq.password}")
    private String rabbitmq_Password;


    public static final String ECHO_OUT_RECEIVER_QUEUE = "echoOutReceiverQueue";
    public static final String ECHO_OUT_STATION_DATA_QUEUE = "echoOutStationDataQueue";
    public static final String ECHO_OUT_QUEUE_PDF_GEN = "generatePDF";

    @Bean
    public Queue echoInQueueID(){ return new Queue(ECHO_OUT_RECEIVER_QUEUE, false); }

    @Bean
    public Queue echoCollectionQueue(){ return new Queue(ECHO_OUT_STATION_DATA_QUEUE, false);}

    @Bean
    public Queue echoOUTQUEUEID(){ return new Queue(ECHO_OUT_QUEUE_PDF_GEN, false);}

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmq_Host, RABBITMQ_PORT);
        connectionFactory.setUsername(rabbitmq_Username);
        connectionFactory.setPassword(rabbitmq_Password);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        //rabbitTemplate.setDefaultReceiveQueue(String.valueOf(ECHO_OUT_QUEUE_PDF));
        return rabbitTemplate;
    }

}
