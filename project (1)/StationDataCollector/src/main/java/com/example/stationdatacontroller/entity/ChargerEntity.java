package com.example.stationdatacontroller.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "charge")
public class ChargerEntity {

    public ChargerEntity(){

    }

    public ChargerEntity(int id, float kwh, int customer_id) {
        this.id = id;
        this.kwh = kwh;
        this.customer_id = customer_id;
    }

    @Id
    private int id;
    private float kwh;
    private int customer_id;

    public void setId(int id) {
        this.id = id;
    }

    public float getKwh() {
        return kwh;
    }

    public void setKwh(float kwh) {
        this.kwh = kwh;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }


}
