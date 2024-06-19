package com.example.datacollectiondispatcher.Stationservice;

import com.example.datacollectiondispatcher.CStationsRepository.CStationsRepository;
import com.example.datacollectiondispatcher.config.RabbitMQConfig;
import com.example.datacollectiondispatcher.entity.StationEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetCStations {

    private final RabbitTemplate rabbit;

    private final CStationsRepository cStationsRepository;

    @Autowired
    public GetCStations(RabbitTemplate rabbit, CStationsRepository cStationsRepository) {
        this.rabbit = rabbit;
        this.cStationsRepository = cStationsRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.ECHO_IN_QUEUE_ID)
    public void sendChargingStations(String message){
        List<StationEntity> stationEntities =cStationsRepository.findAll()
                .stream().map(
                        stationEntity -> new StationEntity(
                                stationEntity.getDb_url()
                        )
                ).toList();
        //iterate over the length of the list to process each db_url seperately
        for (StationEntity stationEntity : stationEntities) {
            System.out.println("DB URL: " + stationEntity.getDb_url() + "|" + "customer:" + message);
            rabbit.convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_ID, stationEntity.getDb_url());
        }

        rabbit.convertAndSend(RabbitMQConfig.ECHO_OUT_QUEUE_ID, message.toString());

    }
}
