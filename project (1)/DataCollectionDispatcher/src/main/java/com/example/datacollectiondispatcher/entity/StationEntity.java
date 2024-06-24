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

    private double lat;

    private double lng;

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

    public double getLat() {
        return lat;
    }

    public void setLat(double latitude) {
        this.lat = latitude;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double longitude) {
        this.lng = longitude;
    }
}
