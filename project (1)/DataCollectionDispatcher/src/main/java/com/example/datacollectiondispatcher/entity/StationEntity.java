package com.example.datacollectiondispatcher.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "station")
public class StationEntity {

    public StationEntity(){

    }

    public StationEntity(String db_url) {
        this.db_url = db_url;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    private String db_url;

    private double latitude;

    private double longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDb_url() {
        return db_url;
    }

    public void setDb_url(String db_url) {
        this.db_url = db_url;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
