package com.example.stationdatacontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class StationDataControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StationDataControllerApplication.class, args);
    }

}
