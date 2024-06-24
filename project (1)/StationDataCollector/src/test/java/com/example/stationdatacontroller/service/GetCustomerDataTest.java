package com.example.stationdatacontroller.service;

import com.example.stationdatacontroller.config.RabbitMQConfig;
import com.example.stationdatacontroller.entity.ChargerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCustomerDataTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    @Qualifier("DB1")
    private JdbcTemplate jdbcTemplate1;

    @Mock
    @Qualifier("DB2")
    private JdbcTemplate jdbcTemplate2;

    @Mock
    @Qualifier("DB3")
    private JdbcTemplate jdbcTemplate3;

    @InjectMocks
    private GetCustomerData getCustomerData;

    @BeforeEach
    void setUp() {
        getCustomerData = new GetCustomerData(rabbitTemplate, jdbcTemplate1, jdbcTemplate2, jdbcTemplate3);
    }

    @Test
    public void testGetCustomerData_DB1() {
        String message = "DB URL:30011|customer:12345";
        int customerID = 12345;

        List<ChargerEntity> mockCustomerData = new ArrayList<>();
        mockCustomerData.add(new ChargerEntity(1, 10.5f, customerID));
        mockCustomerData.add(new ChargerEntity(2, 5.0f, customerID));

        when(jdbcTemplate1.query(anyString(), any(RowMapper.class), eq(customerID))).thenReturn(mockCustomerData);

        getCustomerData.GetCustomerData(message);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.ECHO_OUT_STATION_DATA_QUEUE),
                eq("The customer with the ID:12345,used 15.5,on the charging station1")
        );
    }

    @Test
    public void testGetCustomerData_DB2() {
        String message = "DB URL:30012|customer:12345";
        int customerID = 12345;

        List<ChargerEntity> mockCustomerData = new ArrayList<>();
        mockCustomerData.add(new ChargerEntity(1, 10.5f, customerID));
        mockCustomerData.add(new ChargerEntity(2, 5.0f, customerID));

        when(jdbcTemplate2.query(anyString(), any(RowMapper.class), eq(customerID))).thenReturn(mockCustomerData);

        getCustomerData.GetCustomerData(message);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.ECHO_OUT_STATION_DATA_QUEUE),
                eq("The customer with the ID:12345,used 15.5,on the charging station2")
        );
    }

    @Test
    public void testGetCustomerData_DB3() {
        String message = "DB URL:30013|customer:12345";
        int customerID = 12345;

        List<ChargerEntity> mockCustomerData = new ArrayList<>();
        mockCustomerData.add(new ChargerEntity(1, 10.5f, customerID));
        mockCustomerData.add(new ChargerEntity(2, 5.0f, customerID));

        when(jdbcTemplate3.query(anyString(), any(RowMapper.class), eq(customerID))).thenReturn(mockCustomerData);

        getCustomerData.GetCustomerData(message);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.ECHO_OUT_STATION_DATA_QUEUE),
                eq("The customer with the ID:12345,used 15.5,on the charging station3")
        );
    }

    @Test
    public void testGetCustomerData_InvalidMessage() {
        String message = "Invalid message";

        getCustomerData.GetCustomerData(message);

        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString());
        verify(jdbcTemplate1, never()).query(anyString(), any(RowMapper.class), anyInt());
        verify(jdbcTemplate2, never()).query(anyString(), any(RowMapper.class), anyInt());
        verify(jdbcTemplate3, never()).query(anyString(), any(RowMapper.class), anyInt());
    }

    @Test
    public void testGetCustomerData_NoMatchingDBUrl() {
        String message = "DB URL:30014|customer:12345";

        getCustomerData.GetCustomerData(message);

        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString());
        verify(jdbcTemplate1, never()).query(anyString(), any(RowMapper.class), anyInt());
        verify(jdbcTemplate2, never()).query(anyString(), any(RowMapper.class), anyInt());
        verify(jdbcTemplate3, never()).query(anyString(), any(RowMapper.class), anyInt());
    }
}
