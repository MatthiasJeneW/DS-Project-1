package com.example.stationdatacontroller.config;

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


    public static final String ECHO_IN_QUEUE_ID = "getCustomerDetails";

    public static final String ECHO_OUT_QUEUE_PDF = "generatePDF";

    @Bean
    public Queue echoInQueueID(){ return new Queue(ECHO_IN_QUEUE_ID, false); }

    @Bean
    public Queue echoOUTQUEUEID(){ return new Queue(ECHO_OUT_QUEUE_PDF, false);}

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
        rabbitTemplate.setDefaultReceiveQueue(String.valueOf(ECHO_OUT_QUEUE_PDF));
        return rabbitTemplate;
    }

}
