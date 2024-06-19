package com.example.datacollectiondispatcher.Stationservice;



import com.example.datacollectiondispatcher.CStationsRepository.CStationsRepository;
import com.example.datacollectiondispatcher.config.RabbitMQConfig;
import com.example.datacollectiondispatcher.entity.StationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetCStationsTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private CStationsRepository mockedcStationsRepository;

    @InjectMocks
    private GetCStations getCStations;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        getCStations = new GetCStations(rabbitTemplate, mockedcStationsRepository);
    }

    @Test
    public void testSendChargingStations(){
        String message = "TestMessage";
        StationEntity stationEntity1 = new StationEntity("db1");
        StationEntity stationEntity2 = new StationEntity("db2");
        StationEntity stationEntity3 = new StationEntity("db3");
        when(mockedcStationsRepository.findAll()).thenReturn(List.of(stationEntity1, stationEntity2, stationEntity3));

        getCStations.sendChargingStations(message);

        verify(mockedcStationsRepository).findAll();
        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_ID, "db1");
        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_ID, "db2");
        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_ID, "db3");
        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_ID, message);

    }

}