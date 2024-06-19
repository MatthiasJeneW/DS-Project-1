package com.example.stationdatacontroller.service;

import com.example.stationdatacontroller.config.RabbitMQConfig;
import com.example.stationdatacontroller.entity.ChargerEntity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GetCustomerDataTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private JdbcTemplate jdbcTemplate1;

    @Mock
    private JdbcTemplate jdbcTemplate2;

    @Mock
    private JdbcTemplate jdbcTemplate3;

    @InjectMocks
    private GetCustomerData getCustomerData;

    @BeforeEach
    void setUp() {
        getCustomerData = new GetCustomerData(rabbitTemplate, jdbcTemplate1, jdbcTemplate2, jdbcTemplate3);
    }

    @Test
    public void testGetCustomerData_DB1(){
        String message = "DB URL:30011|customer:12345";
        int customerID = 12345;

        List<ChargerEntity> mockCustomerData = new ArrayList<>();
        mockCustomerData.add(new ChargerEntity(1, 10.5f, customerID));
        mockCustomerData.add(new ChargerEntity(2, 5.0f, customerID));

        when(jdbcTemplate1.query(anyString(), any(Object[].class), any(RowMapper.class))).thenAnswer(invocation -> {
            RowMapper<ChargerEntity> rowMapper = invocation.getArgument(2);
            List<ChargerEntity> result = new ArrayList<>();
            ResultSet rs = mock(ResultSet.class);
            when(rs.next()).thenReturn(true, true, false);
            when(rs.getInt("id")).thenReturn(1, 2);
            when(rs.getFloat("kwh")).thenReturn(10.5f, 5.0f);
            when(rs.getInt("customer_id")).thenReturn(customerID, customerID);
            result.add(rowMapper.mapRow(rs, 1));
            result.add(rowMapper.mapRow(rs, 2));
            return result;
        });



        getCustomerData.GetCustomerData(message);

        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_ID, "The customer with the ID:12345used15.5");
    }
}