package com.busticket.amedora.busticketverify.model;

/**
 * Created by Amedora on 12/4/2015.
 */
public class Bus {
    public int route_id;
    public int bus_id;
    public String plate_no;
    public String driver;
    public String conductor;

    public Bus(){

    }

    public void setBus_id(int bus_id) {
        this.bus_id = bus_id;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getBus_id() {
        return bus_id;
    }

    public String getConductor() {
        return conductor;
    }

    public String getDriver() {
        return driver;
    }

    public int getRoute_id() {
        return route_id;
    }

    public String getPlate_no() {
        return plate_no;
    }
}
