package com.busticket.amedora.busticketverify.model;

/**
 * Created by Amedora on 12/16/2015.
 */
public class Route {

    public int id;
    public int route_id;
    public String short_name;
    public String name;
    public String distance;
    public String geodata;
    public String description;

    public Route(){

    }

    public int getId() {
        return id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public String getName() {
        return name;
    }

    public String getShort_name(){
        return short_name;
    }

    public String getDistance() {
        return distance;
    }

    public String getDescription() {
        return description;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGeodata(String geodata) {
        this.geodata = geodata;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
