package com.example.datacollectiondispatcher.CStationsRepository;


import com.example.datacollectiondispatcher.entity.StationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CStationsRepository extends CrudRepository<StationEntity, Integer> {
    List<StationEntity> findAll();
}
