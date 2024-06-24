package com.example.stationdatacontroller.service;

import com.example.stationdatacontroller.config.DataSourceConfig;
import com.example.stationdatacontroller.config.RabbitMQConfig;
import com.example.stationdatacontroller.entity.ChargerEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


@Service
public class GetCustomerData {

    private final RabbitTemplate rabbitTemplate;

    private final JdbcTemplate jdbcTemplate1;
    private final JdbcTemplate jdbcTemplate2;
    private final JdbcTemplate jdbcTemplate3;

    @Autowired
    public GetCustomerData(RabbitTemplate rabbitTemplate, @Qualifier("DB1") JdbcTemplate jdbcTemplate1, @Qualifier("DB2") JdbcTemplate jdbcTemplate2, @Qualifier("DB3") JdbcTemplate jdbcTemplate3) {
        this.rabbitTemplate = rabbitTemplate;
        this.jdbcTemplate1 = jdbcTemplate1;
        this.jdbcTemplate2 = jdbcTemplate2;
        this.jdbcTemplate3 = jdbcTemplate3;
    }

    @RabbitListener(queues = RabbitMQConfig.ECHO_OUT_STATION_QUEUE)
    public void GetCustomerData(String message) {

        System.out.println(message);

        String customerQuery = "SELECT id, kwh, customer_id FROM charge WHERE customer_id = ?";

        String[] messageParts = message.split("\\|");

        if (messageParts.length < 2) {

            return;
        }

        String customerPart = messageParts[1];
        String[] customerParts = customerPart.split(":");

        if (customerParts.length < 2) {

            return;
        }

        int customerID;
        try {
            customerID = Integer.parseInt(customerParts[1].trim());
        } catch (NumberFormatException e) {

            return;
        }

        String dbUrl = messageParts[0];
        List<ChargerEntity> customerData = new ArrayList<>();

        if (message.contains("30011")) {
            dbUrl = String.valueOf(1);
            customerData = jdbcTemplate1.query(customerQuery, (ResultSet rs, int rowNum) -> new ChargerEntity(
                    rs.getInt("id"),
                    rs.getFloat("kwh"),
                    rs.getInt("customer_id")
            ), customerID);
        } else if (message.contains("30012")) {
            dbUrl = String.valueOf(2);
            customerData = jdbcTemplate2.query(customerQuery, (ResultSet rs, int rowNum) -> new ChargerEntity(
                    rs.getInt("id"),
                    rs.getFloat("kwh"),
                    rs.getInt("customer_id")
            ), customerID);
        } else if (message.contains("30013")) {
            dbUrl = String.valueOf(3);
            customerData = jdbcTemplate3.query(customerQuery, (ResultSet rs, int rowNum) -> new ChargerEntity(
                    rs.getInt("id"),
                    rs.getFloat("kwh"),
                    rs.getInt("customer_id")
            ), customerID);
        } else {
            System.out.println("No matching DB-Url found.");
            return;
        }

        float kwhUsed = 0;
        for (ChargerEntity customerDatum : customerData) {
            kwhUsed += customerDatum.getKwh();
        }

        String newMessage = "The customer with the ID:" + customerID + ",used " + kwhUsed + ",on the charging station" + dbUrl;
        System.out.println(newMessage);
        rabbitTemplate.convertAndSend(RabbitMQConfig.ECHO_OUT_STATION_DATA_QUEUE, newMessage);
    }
}
