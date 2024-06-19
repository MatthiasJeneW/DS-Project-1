package com.example.stationdatacontroller.service;

import com.example.stationdatacontroller.config.DataSourceConfig;
import com.example.stationdatacontroller.config.RabbitMQConfig;
import com.example.stationdatacontroller.entity.ChargerEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GetCustomerData {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    DataSourceConfig dataSourceConfig;

    private final JdbcTemplate jdbcTemplate1;

    private final JdbcTemplate jdbcTemplate2;

    private final JdbcTemplate jdbcTemplate3;

    @Autowired
    public GetCustomerData(RabbitTemplate rabbitTemplate, @Qualifier("DB1")JdbcTemplate jdbcTemplate1, @Qualifier("DB2") JdbcTemplate jdbcTemplate2, @Qualifier("DB3")JdbcTemplate jdbcTemplate3) {
        this.rabbitTemplate = rabbitTemplate;
        this.jdbcTemplate1 = jdbcTemplate1;
        this.jdbcTemplate2 = jdbcTemplate2;
        this.jdbcTemplate3 = jdbcTemplate3;
    }

    @RabbitListener(queues = RabbitMQConfig.ECHO_IN_QUEUE_ID)
    public void GetCustomerData(String message) {
        var customer = """
                SELECT * FROM charge
                where customer_id = ?
                """;

        List<ChargerEntity> customerData = new ArrayList<>();


        String[] messageParts = message.split("|");
        int customerID = Integer.parseInt(messageParts[0].split(":")[1].trim());

        if(message.contains("30011")){
            customerData = jdbcTemplate1.query(customer, (ResultSet rs, int rownumb)-> new ChargerEntity(
                    rs.getInt("id"),
                    rs.getFloat("kwh"),
                    rs.getInt("customer_id")
            ),customerID);
        }
        else if(message.contains("300012")){
            customerData = jdbcTemplate1.query(customer, (ResultSet rs, int rownumb)-> new ChargerEntity(
                    rs.getInt("id"),
                    rs.getFloat("kwh"),
                    rs.getInt("customer_id")
            ),customerID);
        }
        else if(message.contains("30013")){
            customerData = jdbcTemplate1.query(customer, (ResultSet rs, int rownumb)-> new ChargerEntity(
                    rs.getInt("id"),
                    rs.getFloat("kwh"),
                    rs.getInt("customer_id")
            ),customerID);
        }
        else{
            System.out.println("No matching DB-Url found.");
            return;
        }
        float khwUsed = 0;
        for (ChargerEntity customerDatum : customerData) {
            khwUsed+=customerDatum.getKwh();
        }

        String newMessage = "The customer with the ID:" + customerID + "used" + khwUsed;
        rabbitTemplate.convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_ID, newMessage);
    }

}
